package com.sqmusicplus.v3.mcp;

import io.modelcontextprotocol.json.McpJsonDefaults;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.HttpServletStreamableServerTransportProvider;
import io.modelcontextprotocol.server.transport.ServerTransportSecurityValidator;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static io.modelcontextprotocol.spec.McpSchema.CallToolResult;

/**
 * MCP (Model Context Protocol) 服务器配置
 * <p>
 * 同时支持两种传输方式:
 * <ul>
 *   <li>STDIO 传输 - 用于本地 AI 助手 (Claude Desktop 等)</li>
 *   <li>HTTP 传输 (Streamable HTTP) - 用于远程 AI 代理</li>
 * </ul>
 * 通过 application.yml 中的 mcp.enabled 开关控制
 * </p>
 *
 * @author SQ
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "mcp", name = "enabled", havingValue = "true", matchIfMissing = false)
public class McpConfig {

    @Autowired
    private ControllerMcpBridge controllerMcpBridge;

    @Autowired
    private com.sqmusicplus.v3.config.JwtUtil jwtUtil;

    @Value("${mcp.http-path:/mcp}")
    private String mcpHttpPath;

    @Value("${mcp.stdio-enabled:false}")
    private boolean stdioEnabled;

    private McpSyncServer stdioServer;
    private McpSyncServer httpServer;

    @PostConstruct
    public void initStdioTransport() {
        log.info("MCP 功能已启用, HTTP 传输路径: {}", mcpHttpPath);

        if (stdioEnabled) {
            try {
                var stdioTransport = new StdioServerTransportProvider(McpJsonDefaults.getMapper());
                stdioServer = buildSyncServer(stdioTransport);
                log.info("MCP STDIO 服务器已启动");
            } catch (Exception e) {
                log.error("MCP STDIO 服务器启动失败", e);
            }
        } else {
            log.info("MCP STDIO 传输未启用 (mcp.stdio-enabled=false)");
        }
    }

    @Bean
    public ServletRegistrationBean<HttpServletStreamableServerTransportProvider> mcpHttpServlet() {
        try {
            // HTTP 传输需要 JWT Token 验证 (Authorization: Bearer <token>)
            // STDIO 传输不需要 Token
            var jwtValidator = jwtUtil;
            ServerTransportSecurityValidator securityValidator = headers -> {
                List<String> authHeaders = headers.get("sqmusic");
                if (authHeaders == null || authHeaders.isEmpty()) {
                    throw new io.modelcontextprotocol.server.transport.ServerTransportSecurityException(
                            401, "缺少 sqmusic 请求头");
                }
                String authValue = authHeaders.get(0);

                if (!jwtValidator.validateToken(authValue)) {
                    throw new io.modelcontextprotocol.server.transport.ServerTransportSecurityException(
                            403, "JWT Token 无效或已过期");
                }            log.info("MCP HTTP JWT Token 验证已启用");

            };
            var httpTransport = HttpServletStreamableServerTransportProvider.builder()
                    .jsonMapper(McpJsonDefaults.getMapper())
                    .mcpEndpoint(mcpHttpPath)
                    .securityValidator(securityValidator)
                    .build();
            httpServer = buildSyncServer(httpTransport);
            var bean = new ServletRegistrationBean<>(httpTransport, mcpHttpPath);
            bean.setLoadOnStartup(1);
            log.info("MCP HTTP 服务器已注册, 路径: {}", mcpHttpPath);
            return bean;
        } catch (Exception e) {
            log.error("MCP HTTP 服务器注册失败: {}", e.getMessage(), e);
            var fallback = new ServletRegistrationBean<HttpServletStreamableServerTransportProvider>();
            fallback.setEnabled(false);
            return fallback;
        }
    }

    /**
     * 注册 Accept 头过滤器，运行在 MCP Servlet 之前
     * <p>
     * 自动补全 Accept 头中的 application/json，解决 MCP SDK 内部
     * 内容协商导致的 HTTP 406 Not Acceptable 问题。
     * </p>
     */
    @Bean
    public FilterRegistrationBean<McpAcceptHeaderFilter> mcpAcceptHeaderFilter() {
        FilterRegistrationBean<McpAcceptHeaderFilter> reg = new FilterRegistrationBean<>();
        reg.setFilter(new McpAcceptHeaderFilter(mcpHttpPath));
        reg.addUrlPatterns(mcpHttpPath + "/*");
        reg.setName("mcpAcceptHeaderFilter");
        reg.setOrder(0);
        log.info("MCP Accept 头过滤器已注册, 路径: {}/**", mcpHttpPath);
        return reg;
    }

    @PreDestroy
    public void shutdown() {
        if (stdioServer != null) {
            try { stdioServer.closeGracefully(); } catch (Exception e) { log.warn("MCP STDIO 关闭异常", e); }
        }
        if (httpServer != null) {
            try { httpServer.closeGracefully(); } catch (Exception e) { log.warn("MCP HTTP 关闭异常", e); }
        }
        log.info("MCP 服务器已关闭");
    }

    // ======================== 共享构建方法 ========================

    @SuppressWarnings("unchecked")
    private McpSyncServer buildSyncServer(Object transport) {
        var registrations = controllerMcpBridge.discoverTools();

        Object builder;
        if (transport instanceof io.modelcontextprotocol.spec.McpStreamableServerTransportProvider stp) {
            builder = McpServer.sync(stp);
        } else if (transport instanceof io.modelcontextprotocol.spec.McpServerTransportProvider tp) {
            builder = McpServer.sync(tp);
        } else {
            throw new IllegalArgumentException("未知传输类型: " + transport.getClass());
        }

        try {
            var bc = builder.getClass();
            builder = bc.getMethod("serverInfo", McpSchema.Implementation.class)
                    .invoke(builder, McpSchema.Implementation.builder("simple-music-server", "3.0").build());
            builder = bc.getMethod("capabilities", McpSchema.ServerCapabilities.class)
                    .invoke(builder, McpSchema.ServerCapabilities.builder().tools(true).build());

            // 内置帮助工具 (mcp_help) - 始终第一个注册
            builder = bc.getMethod("toolCall", McpSchema.Tool.class, BiFunction.class)
                    .invoke(builder, defineHelpTool(), handleHelp());

            // 动态发现的控制器工具
            for (var reg : registrations) {
                builder = bc.getMethod("toolCall", McpSchema.Tool.class, BiFunction.class)
                        .invoke(builder, reg.tool(), reg.handler());
            }

            return (McpSyncServer) bc.getMethod("build").invoke(builder);
        } catch (Exception e) {
            throw new RuntimeException("构建 MCP 服务器失败", e);
        }
    }

    // ======================== 内置 mcp_help 工具 ========================

    private McpSchema.Tool defineHelpTool() {
        return McpSchema.Tool.builder("mcp_help", Map.of(
                "type", "object",
                "properties", Map.of(
                        "tool", Map.of("type", "string",
                                "description", "要查询的工具名称（可选，不填则列出所有工具）")
                )
        )).title("MCP 帮助")
                .description("查看所有可用 MCP 工具及其参数说明，或查询指定工具的详细用法")
                .build();
    }

    @SuppressWarnings("unchecked")
    private BiFunction<Object,
            McpSchema.CallToolRequest, CallToolResult> handleHelp() {
        return (exchange, req) -> {
            try {
                Map<String, Object> args = req.arguments() != null ? req.arguments() : Map.of();
                String filterTool = (String) args.get("tool");

                var registrations = controllerMcpBridge.discoverTools();

                StringBuilder sb = new StringBuilder();
                sb.append("# 🎵 Simple Music Server - MCP 使用指南\n\n");

                sb.append("## 连接方式\n\n");
                sb.append("| 方式 | 地址 | 说明 |\n");
                sb.append("|------|------|------|\n");
                sb.append("| HTTP | `").append(mcpHttpPath).append("` | 远程 AI 代理连接 |\n");
                sb.append("| STDIO | 标准输入/输出 | 本地 AI 助手连接 |\n\n");

                sb.append("## 参数传递规则\n\n");
                sb.append("Controller 参数 → MCP 工具参数映射：\n\n");
                sb.append("| Controller 参数 | MCP 传参方式 | 示例 |\n");
                sb.append("|----------------|-------------|------|\n");
                sb.append("| `@RequestBody POJO` | 展平为各字段 | `{\"plugName\":\"kg\"}` |\n");
                sb.append("| `@RequestParam` | 直接传参 | `{\"pageIndex\":1}` |\n");
                sb.append("| `@PathVariable` | 直接传参 | `{\"id\":\"xxx\"}` |\n");
                sb.append("| 无注解 POJO | 前缀 `.` 分隔 | `{\"paramName.field\":\"val\"}` |\n\n");

                if (filterTool != null && !filterTool.isEmpty()) {
                    sb.append("## 工具详情: `").append(filterTool).append("`\n\n");
                    boolean found = false;
                    for (var reg : registrations) {
                        if (reg.tool().name().equals(filterTool)) {
                            appendToolDetail(sb, reg.tool());
                            found = true;
                            break;
                        }
                    }
                    if (!found) sb.append("未找到工具 `").append(filterTool).append("`\n");
                } else {
                    sb.append("## 可用工具 (").append(registrations.size()).append(" 个)\n\n");
                    for (var reg : registrations) {
                        appendToolSummary(sb, reg.tool());
                    }
                    sb.append("> 提示: 调用 `mcp_help` 并传入 `tool` 参数可查看指定工具的完整 JSON Schema\n");
                }

                return CallToolResult.builder()
                        .content(List.of(McpSchema.TextContent.builder(sb.toString()).build()))
                        .build();
            } catch (Exception e) {
                return CallToolResult.builder()
                        .content(List.of(McpSchema.TextContent.builder("获取帮助失败: " + e.getMessage()).build()))
                        .isError(true).build();
            }
        };
    }

    private void appendToolSummary(StringBuilder sb, McpSchema.Tool tool) {
        sb.append("### `").append(tool.name()).append("`\n");
        sb.append("> ").append(tool.description() != null ? tool.description() : "").append("\n\n");

        @SuppressWarnings("unchecked")
        var props = tool.inputSchema() != null && tool.inputSchema().get("properties") instanceof Map<?, ?> m
                ? (Map<String, Object>) m : Map.<String, Object>of();
        @SuppressWarnings("unchecked")
        var required = tool.inputSchema() != null && tool.inputSchema().get("required") instanceof List<?> r
                ? (List<String>) r : List.<String>of();

        if (!props.isEmpty()) {
            sb.append("| 参数 | 类型 | 必填 | 说明 |\n");
            sb.append("|------|------|------|------|\n");
            for (var e : props.entrySet()) {
                @SuppressWarnings("unchecked")
                var prop = e.getValue() instanceof Map<?, ?> m ? (Map<String, Object>) m : Map.<String, Object>of();
                String type = String.valueOf(prop.getOrDefault("type", "?"));
                String desc = String.valueOf(prop.getOrDefault("description", ""));
                boolean isReq = required.contains(e.getKey());
                sb.append("| `").append(e.getKey()).append("` | `").append(type)
                        .append("` | ").append(isReq ? "✅" : " ")
                        .append(" | ").append(desc).append(" |\n");
            }
        }
        sb.append("\n");
    }

    private void appendToolDetail(StringBuilder sb, McpSchema.Tool tool) {
        sb.append("**名称**: `").append(tool.name()).append("`\n\n");
        sb.append("**描述**: ").append(tool.description() != null ? tool.description() : "无").append("\n\n");
        sb.append("**参数 Schema**:\n```json\n");
        try {
            sb.append(new com.fasterxml.jackson.databind.ObjectMapper()
                    .writerWithDefaultPrettyPrinter().writeValueAsString(tool.inputSchema()));
        } catch (Exception e) {
            sb.append(tool.inputSchema());
        }
        sb.append("\n```\n");
    }
}

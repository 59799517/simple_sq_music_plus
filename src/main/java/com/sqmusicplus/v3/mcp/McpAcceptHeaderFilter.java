package com.sqmusicplus.v3.mcp;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;

/**
 * MCP Accept 头过滤器
 * <p>
 * 解决 MCP 客户端未发送 Accept: application/json 导致 HTTP 406 的问题。
 * 请求到达 HttpServletStreamableServerTransportProvider 之前，
 * 自动补全 Accept 头，避免 SDK 库内部的内容协商拒绝请求。
 * </p>
 * <ul>
 *   <li>Accept 已包含 application/json → 正常放行</li>
 *   <li>Accept 缺失或为空 → 自动补全 Accept: application/json</li>
 *   <li>Accept 有值但不含 application/json → 自动追加 , application/json</li>
 * </ul>
 *
 * @author SQ
 */
@Slf4j
public class McpAcceptHeaderFilter implements Filter {

    /** MCP 路径前缀 */
    private final String mcpPath;

    public McpAcceptHeaderFilter(String mcpPath) {
        this.mcpPath = mcpPath;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        if (!(servletRequest instanceof HttpServletRequest req)
                || !(servletResponse instanceof HttpServletResponse resp)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String requestURI = req.getRequestURI();

        // 只对 MCP 路径生效 - 使用精确匹配而非 startsWith
        // 确保 /mcp 和 /mcp/ 的请求被处理，但 /xxxmcp 不会被误匹配
        if (!requestURI.equals(mcpPath) && !requestURI.startsWith(mcpPath + "/")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String acceptHeader = req.getHeader("Accept");

        // ✅ Accept 已包含 application/json → 放行
        if (acceptHeader != null && acceptHeader.contains("application/json")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // 🔧 Accept 缺失或为空 → 自动补全
        if (acceptHeader == null || acceptHeader.isBlank()) {
            log.debug("MCP 请求缺少 Accept 头，自动添加 application/json: {} {}", req.getMethod(), requestURI);
            filterChain.doFilter(new AcceptHeaderWrappedRequest(req, "application/json"), servletResponse);
            return;
        }

        // 🔧 Accept 有值但不含 application/json → 自动追加
        log.info("MCP 请求 Accept 头 ({}) 缺少 application/json，自动补全: {} {}",
                acceptHeader, req.getMethod(), requestURI);
        String mergedAccept = acceptHeader + ", application/json";
        filterChain.doFilter(new AcceptHeaderWrappedRequest(req, mergedAccept), servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        // 无需初始化
    }

    @Override
    public void destroy() {
        // 无需销毁
    }

    // ======================== 请求包装器 ========================

    /**
     * 包装 HttpServletRequest，覆盖 Accept 头
     */
    private static class AcceptHeaderWrappedRequest extends HttpServletRequestWrapper {

        private final String acceptHeader;

        public AcceptHeaderWrappedRequest(HttpServletRequest request, String acceptHeader) {
            super(request);
            this.acceptHeader = acceptHeader;
        }

        @Override
        public String getHeader(String name) {
            if ("accept".equalsIgnoreCase(name)) {
                return acceptHeader;
            }
            return super.getHeader(name);
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            if ("accept".equalsIgnoreCase(name)) {
                return Collections.enumeration(List.of(acceptHeader));
            }
            return super.getHeaders(name);
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            List<String> names = Collections.list(super.getHeaderNames());
            if (names.stream().noneMatch(h -> "accept".equalsIgnoreCase(h))) {
                names.add("Accept");
            }
            return Collections.enumeration(names);
        }

        @Override
        public int getIntHeader(String name) {
            if ("accept".equalsIgnoreCase(name)) {
                return 0;
            }
            return super.getIntHeader(name);
        }

        @Override
        public long getDateHeader(String name) {
            if ("accept".equalsIgnoreCase(name)) {
                return -1;
            }
            return super.getDateHeader(name);
        }
    }
}

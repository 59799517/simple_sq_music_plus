package com.sqmusicplus.v3.mcp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sqmusicplus.v3.config.AjaxResult;
import io.modelcontextprotocol.spec.McpSchema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static io.modelcontextprotocol.spec.McpSchema.CallToolResult;

@Slf4j
@Component
public class ControllerMcpBridge {

    @Autowired
    private ApplicationContext applicationContext;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<Method, List<ParamInfo>> paramCache = new ConcurrentHashMap<>();

    public List<McpToolRegistration> discoverTools() {
        List<McpToolRegistration> registrations = new ArrayList<>();
        Map<String, Object> controllers = applicationContext.getBeansWithAnnotation(RestController.class);

        for (Map.Entry<String, Object> entry : controllers.entrySet()) {
            Object controller = entry.getValue();
            Class<?> controllerClass = getControllerClass(controller);
            String basePath = resolveBasePath(controllerClass);
            String controllerName = controllerClass.getSimpleName()
                    .replace("Controller", "").toLowerCase();

            for (Method method : controllerClass.getDeclaredMethods()) {
                RequestMappingInfo mappingInfo = resolveRequestMapping(method);
                if (mappingInfo == null) continue;

                String toolName = controllerName + "_" + method.getName();
                String description = controllerClass.getSimpleName() + " > " + method.getName()
                        + " [" + mappingInfo.httpMethod + " " + basePath + mappingInfo.path + "]";
                McpSchema.Tool tool = buildTool(toolName, description, method);

                registrations.add(new McpToolRegistration(tool, (exchange, req) -> {
                    try {
                        return invokeController(controller, method, req);
                    } catch (Exception e) {
                        return CallToolResult.builder()
                                .content(List.of(McpSchema.TextContent.builder("error: " + e.getMessage()).build()))
                                .isError(true).build();
                    }
                }));
            }
        }
        log.info("MCP bridge registered {} tools", registrations.size());
        return registrations;
    }

    public record McpToolRegistration(
            McpSchema.Tool tool,
            java.util.function.BiFunction<Object, McpSchema.CallToolRequest, CallToolResult> handler
    ) {}

    private Class<?> getControllerClass(Object controller) {
        Class<?> clazz = controller.getClass();
        while (clazz.isSynthetic() || clazz.getName().contains("$$")) clazz = clazz.getSuperclass();
        return clazz;
    }

    private String resolveBasePath(Class<?> controllerClass) {
        RequestMapping rm = AnnotationUtils.findAnnotation(controllerClass, RequestMapping.class);
        if (rm != null && rm.value().length > 0) return rm.value()[0];
        if (rm != null && rm.path().length > 0) return rm.path()[0];
        return "";
    }

    private RequestMappingInfo resolveRequestMapping(Method method) {
        RequestMapping rm = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
        if (rm == null) return null;
        String path = rm.value().length > 0 ? rm.value()[0] : (rm.path().length > 0 ? rm.path()[0] : "");
        String httpMethod = "GET";
        if (rm.method().length > 0) httpMethod = rm.method()[0].name();
        else if (method.getAnnotation(PostMapping.class) != null) httpMethod = "POST";
        else if (method.getAnnotation(PutMapping.class) != null) httpMethod = "PUT";
        else if (method.getAnnotation(DeleteMapping.class) != null) httpMethod = "DELETE";
        return new RequestMappingInfo(httpMethod, path);
    }

    private McpSchema.Tool buildTool(String toolName, String description, Method method) {
        List<ParamInfo> params = analyzeParameters(method);
        Map<String, Object> properties = new LinkedHashMap<>();
        List<String> required = new ArrayList<>();
        for (ParamInfo p : params) {
            Map<String, Object> prop = new LinkedHashMap<>();
            prop.put("type", p.jsonType);
            if (p.description != null) prop.put("description", p.description);
            if (!p.enumValues.isEmpty()) prop.put("enum", p.enumValues);
            properties.put(p.name, prop);
            if (p.required) required.add(p.name);
        }
        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("type", "object");
        schema.put("properties", properties);
        if (!required.isEmpty()) schema.put("required", required);
        return McpSchema.Tool.builder(toolName, schema).title(method.getName()).description(description).build();
    }

    private List<ParamInfo> analyzeParameters(Method method) {
        return paramCache.computeIfAbsent(method, m -> {
            List<ParamInfo> result = new ArrayList<>();
            for (Parameter param : m.getParameters()) {
                Class<?> pt = param.getType();
                if (HttpServletRequest.class.isAssignableFrom(pt) || HttpServletResponse.class.isAssignableFrom(pt)) continue;
                RequestBody rb = param.getAnnotation(RequestBody.class);
                if (rb != null) { result.addAll(flattenFields(pt)); continue; }
                RequestParam rp = param.getAnnotation(RequestParam.class);
                if (rp != null) {
                    String n = rp.value().isEmpty() ? rp.name() : rp.value();
                    if (n.isEmpty()) n = param.getName();
                    result.add(new ParamInfo(n, toJsonType(pt), rp.required(), pt.getSimpleName(), List.of()));
                    continue;
                }
                PathVariable pv = param.getAnnotation(PathVariable.class);
                if (pv != null) {
                    String n = pv.value().isEmpty() ? pv.name() : pv.value();
                    if (n.isEmpty()) n = param.getName();
                    result.add(new ParamInfo(n, toJsonType(pt), true, pt.getSimpleName(), List.of()));
                    continue;
                }
                if (!isSimpleType(pt)) result.addAll(flattenPrefixedFields(pt, param.getName()));
                else result.add(new ParamInfo(param.getName(), toJsonType(pt), false, pt.getSimpleName(), List.of()));
            }
            return result;
        });
    }

    private List<ParamInfo> flattenFields(Class<?> type) {
        List<ParamInfo> fields = new ArrayList<>();
        for (Field f : getAllFields(type)) {
            boolean required = false;
            // Check @JsonProperty(required=true)
            JsonProperty jp = f.getAnnotation(JsonProperty.class);
            if (jp != null && jp.required()) required = true;
            // Check jakarta.validation @NotNull / @NotBlank / @NotEmpty
            if (!required) {
                required = f.getAnnotation(jakarta.validation.constraints.NotNull.class) != null
                        || f.getAnnotation(jakarta.validation.constraints.NotBlank.class) != null
                        || f.getAnnotation(jakarta.validation.constraints.NotEmpty.class) != null;
            }
            fields.add(new ParamInfo(f.getName(), toJsonType(f.getType()), required, f.getType().getSimpleName(), getEnumValues(f.getType())));
        }
        return fields;
    }

    private List<ParamInfo> flattenPrefixedFields(Class<?> type, String prefix) {
        List<ParamInfo> fields = new ArrayList<>();
        for (Field f : getAllFields(type))
            fields.add(new ParamInfo(prefix + "." + f.getName(), toJsonType(f.getType()), false, f.getType().getSimpleName(), getEnumValues(f.getType())));
        return fields;
    }

    private List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        Class<?> c = clazz;
        while (c != null && c != Object.class) { fields.addAll(Arrays.asList(c.getDeclaredFields())); c = c.getSuperclass(); }
        return fields;
    }

    private List<String> getEnumValues(Class<?> type) {
        if (type.isEnum()) return Arrays.stream(type.getEnumConstants()).map(Object::toString).toList();
        return List.of();
    }

    private boolean isSimpleType(Class<?> t) {
        return t.isPrimitive() || t == String.class || t == Integer.class || t == Long.class
                || t == Double.class || t == Float.class || t == Boolean.class || t == Date.class || Number.class.isAssignableFrom(t);
    }

    private String toJsonType(Class<?> t) {
        if (t == String.class || t.isEnum()) return "string";
        if (t == Integer.class || t == int.class || t == Long.class || t == long.class) return "integer";
        if (t == Double.class || t == double.class || t == Float.class || t == float.class) return "number";
        if (t == Boolean.class || t == boolean.class) return "boolean";
        if (java.util.Collection.class.isAssignableFrom(t) || t.isArray()) return "array";
        if (java.util.Map.class.isAssignableFrom(t) || com.alibaba.fastjson2.JSONObject.class.isAssignableFrom(t)) return "object";
        return "string";
    }

    @SuppressWarnings("unchecked")
    private CallToolResult invokeController(Object controller, Method method, McpSchema.CallToolRequest request) throws Exception {
        Map<String, Object> args = request.arguments() != null ? request.arguments() : Map.of();
        Parameter[] parameters = method.getParameters();
        List<ParamInfo> params = analyzeParameters(method);
        Object[] invokeArgs = new Object[parameters.length];
        int idx = 0;
        for (int i = 0; i < parameters.length; i++) {
            Class<?> pt = parameters[i].getType();
            if (HttpServletRequest.class.isAssignableFrom(pt) || HttpServletResponse.class.isAssignableFrom(pt)) { invokeArgs[i] = null; continue; }
            if (parameters[i].getAnnotation(RequestBody.class) != null) { invokeArgs[i] = buildFromArgs(pt, args); continue; }
            if (!isSimpleType(pt)) { invokeArgs[i] = buildFromPrefixedArgs(pt, args, parameters[i].getName()); continue; }
            if (idx < params.size()) { invokeArgs[i] = convertArg(args.get(params.get(idx).name), pt); idx++; }
        }
        method.setAccessible(true);
        Object result = method.invoke(controller, invokeArgs);
        if (result instanceof AjaxResult) {
            AjaxResult ajax = (AjaxResult) result;
            Object data = ajax.get("data");
            Object msg = ajax.get("msg");
            Object codeObj = ajax.get("code");
            int code = codeObj instanceof Number ? ((Number) codeObj).intValue() : 0;
            StringBuilder sb = new StringBuilder("## ").append(msg).append("\n\n");
            if (code != 200) sb.append("**状态**: 失败 (code=").append(code).append(")\n**消息**: ").append(msg).append("\n");
            else {
                sb.append("**状态**: 成功\n");
                if (data != null) sb.append("**数据**:\n```json\n").append(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data)).append("\n```\n");
            }
            return CallToolResult.builder().content(List.of(McpSchema.TextContent.builder(sb.toString()).build())).build();
        }
        return CallToolResult.builder()
                .content(List.of(McpSchema.TextContent.builder(
                        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result != null ? result : "ok")).build()))
                .build();
    }

    private Object buildFromArgs(Class<?> type, Map<String, Object> args) {
        try {
            return objectMapper.convertValue(args, type);
        } catch (Exception e) {
            log.error("Failed to build @RequestBody for {}: {}", type.getSimpleName(), e.getMessage());
            return null;
        }
    }

    private Object buildFromPrefixedArgs(Class<?> type, Map<String, Object> args, String prefix) {
        try {
            Object inst = type.getDeclaredConstructor().newInstance();
            for (Field f : getAllFields(type)) {
                Object v = args.get(prefix + "." + f.getName());
                if (v != null) { f.setAccessible(true); f.set(inst, convertArg(v, f.getType())); }
            }
            return inst;
        } catch (Exception e) { return null; }
    }

    @SuppressWarnings("unchecked")
    private Object convertArg(Object value, Class<?> targetType) {
        if (value == null) return null;
        if (targetType.isInstance(value)) return value;
        if (targetType == String.class) return value.toString();
        if (targetType == Integer.class || targetType == int.class) return value instanceof Number ? ((Number) value).intValue() : Integer.parseInt(value.toString());
        if (targetType == Long.class || targetType == long.class) return value instanceof Number ? ((Number) value).longValue() : Long.parseLong(value.toString());
        if (targetType == Double.class || targetType == double.class) return value instanceof Number ? ((Number) value).doubleValue() : Double.parseDouble(value.toString());
        if (targetType == Boolean.class || targetType == boolean.class) return value instanceof Boolean ? (Boolean) value : Boolean.parseBoolean(value.toString());
        if (targetType.isEnum()) return Enum.valueOf((Class<Enum>) targetType, value.toString());
        return value;
    }

    private record RequestMappingInfo(String httpMethod, String path) {}
    private record ParamInfo(String name, String jsonType, boolean required, String description, List<String> enumValues) {}
}

package com.sqmusicplus.v3.utils;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

/**
 * 使用Spring Expression Language (SpEL)处理模板字符串
 */
public class SpelTemplateUtils {
    
    private static final ExpressionParser parser = new SpelExpressionParser();
    
    /**
     * 使用SpEL处理模板字符串
     * @param template 模板字符串，如 "#{artists}/#{album}/#{musicName} - #{artists}"
     * @param params 参数映射
     * @return 替换后的字符串
     */
    public static String formatTemplate(String template, Map<String, Object> params) {
        if (template == null || template.isEmpty() || params == null || params.isEmpty()) {
            return template;
        }
        
        // 创建解析上下文，支持#{...}格式的模板
        TemplateParserContext context = new TemplateParserContext("#{", "}");
        
        // 创建评估上下文并设置变量
        EvaluationContext evaluationContext = new StandardEvaluationContext();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            evaluationContext.setVariable(entry.getKey(), entry.getValue());
        }
        
        // 解析并评估模板
        return parser.parseExpression(template, context).getValue(evaluationContext, String.class);
    }
    
    /**
     * 使用SpEL处理模板字符串（使用${...}格式）
     * @param template 模板字符串，如 "${artists}/${album}/${musicName} - ${artists}"
     * @param params 参数映射
     * @return 替换后的字符串
     */
    public static String formatTemplateWithDollar(String template, Map<String, Object> params) {
        if (template == null || template.isEmpty() || params == null || params.isEmpty()) {
            return template;
        }
        
        // 将${xxx}格式转换为#{#xxx}格式，以适配SpEL变量引用
        String convertedTemplate = template.replaceAll("\\$\\{([^}]+)\\}", "#{#$1}");
        
        // 创建解析上下文，支持#{...}格式的模板
        TemplateParserContext context = new TemplateParserContext("#{", "}");
        
        // 创建评估上下文并设置变量
        EvaluationContext evaluationContext = new StandardEvaluationContext();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            evaluationContext.setVariable(entry.getKey(), entry.getValue());
        }
        
        // 解析并评估模板
        return parser.parseExpression(convertedTemplate, context).getValue(evaluationContext, String.class);
    }
    
    /**
     * 使用SpEL表达式直接处理（无模板上下文）
     * @param expression 表达式，如 "#artists + '/' + #album + '/' + #musicName"
     * @param params 参数映射
     * @return 表达式计算结果
     */
    public static Object evaluateExpression(String expression, Map<String, Object> params) {
        if (expression == null || expression.isEmpty() || params == null || params.isEmpty()) {
            return expression;
        }
        
        // 创建评估上下文并设置变量
        EvaluationContext evaluationContext = new StandardEvaluationContext();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            evaluationContext.setVariable(entry.getKey(), entry.getValue());
        }
        
        // 解析并评估表达式
        return parser.parseExpression(expression).getValue(evaluationContext);
    }
}
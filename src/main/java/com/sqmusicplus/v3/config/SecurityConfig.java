package com.sqmusicplus.v3.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private RequireLoginPathScanner pathScanner;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 自动扫描 @RequireLogin(false) 的路径
        Set<String> annotatedPublicPaths = pathScanner.getPublicPaths();
        
        // 固定的公开路径（登录、版本等）
        List<String> fixedPublicPaths = Arrays.asList(
            "/api/config/login",
            "/api/config/isLogin",
            "/api/version"
        );
        
        // 合并所有公开路径
        List<String> allPublicPaths = annotatedPublicPaths.stream()
                .collect(Collectors.toList());
        allPublicPaths.addAll(fixedPublicPaths);
        
        // 添加静态资源路径
        allPublicPaths.addAll(Arrays.asList(
            "/**/*.html",
            "/**/*.js",
            "/**/*.css",
            "/**/*.png",
            "/**/*.jpg",
            "/**/*.svg",
            "/**/*.json",
            "/**/*.woff",
            "/**/*.woff2",
            "/**/*.ttf",
            "/**/*.otf",
            "/favicon.ico"
        ));
        
        String[] publicPathArray = allPublicPaths.toArray(new String[0]);
        
        log.debug("🔒 Spring Security 配置的公开路径共 {} 个: {}", publicPathArray.length, Arrays.toString(publicPathArray));
        
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // 放行的路径（包括自动扫描的注解路径）
                .requestMatchers(publicPathArray).permitAll()
                // 其他所有请求需要认证
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

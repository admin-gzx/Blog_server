package com.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

/**
 * Web安全配置类
 * 配置Spring Security的安全策略，包括认证提供者、密码编码器、CORS配置和安全过滤器链。
 * 启用Web安全和方法级别的权限控制。
 */
@Configuration
@EnableWebSecurity
// 开启方法级别的权限控制（如 @PreAuthorize("hasRole('ADMIN')")）
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    /** 自动注入用户详情服务，用于获取用户信息 */
    @Autowired
    private UserDetailsService userDetailsService;

    /** 自动注入JWT认证入口点，用于处理未认证的请求 */
    @Autowired
    private JwtAuthEntryPoint unauthorizedHandler;

    /**
     * 创建JWT认证过滤器Bean
     * @return JwtAuthTokenFilter实例
     */
    // JWT 过滤器 Bean
    @Bean
    public JwtAuthTokenFilter authenticationJwtTokenFilter() {
        return new JwtAuthTokenFilter();
    }

    /**
     * 创建认证提供者Bean
     * 关联UserDetailsService和密码编码器，用于用户认证
     * @return AuthenticationProvider实例
     */
    // 认证提供者（关联 UserDetailsService 和密码编码器）
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * 创建认证管理器Bean
     * 用于手动触发认证
     * @param authConfig 认证配置
     * @return AuthenticationManager实例
     * @throws Exception 认证管理器获取异常
     */
    // 认证管理器（从配置中获取，用于手动触发认证）
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * 创建密码编码器Bean
     * 使用BCrypt加密算法
     * @return PasswordEncoder实例
     */
    // 密码编码器（BCrypt 加密）
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 创建CORS配置源Bean
     * 解决跨域问题，开发环境允许所有源，生产环境需指定
     * @return CorsConfigurationSource实例
     */
    // 显式 CORS 配置，解决跨域问题
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*")); // 开发环境允许所有源，生产环境需指定
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setExposedHeaders(Collections.singletonList("X-Token-Expired"));
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * 创建安全过滤器链Bean
     * 配置接口权限、认证逻辑、CORS、CSRF、会话策略等安全策略
     * @param http HttpSecurity对象
     * @return SecurityFilterChain实例
     * @throws Exception 安全过滤器链构建异常
     */
    // 核心安全过滤器链（配置接口权限、认证逻辑）
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 启用 CORS（关联上述 corsConfigurationSource）
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 禁用 CSRF（JWT 无状态认证，无需 CSRF 保护）
                .csrf(csrf -> csrf.disable())
                // 配置未认证时的异常处理器（返回自定义错误信息）
                .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))
                // 配置会话策略：无状态（不创建 Session）
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 配置接口权限规则
                .authorizeHttpRequests(auth -> auth
                        // 1. 业务相关公开接口（保持原有逻辑）
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/test/**").permitAll()
                        .requestMatchers("/api/articles/**").permitAll()
                        .requestMatchers("/api/categories/**").permitAll()
                        .requestMatchers("/api/tags/**").permitAll()
                        .requestMatchers("/api/users/public/**").permitAll()
                        .requestMatchers("/api/comments/**").permitAll()
                        // 2. SpringDoc OpenAPI 文档路径（必须放行）
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/v3/api-docs.yaml").permitAll()
                        // 3. 其他所有接口都需要认证
                        .anyRequest().authenticated()
                )
                // 注册认证提供者（使用 DaoAuthenticationProvider）
                .authenticationProvider(authenticationProvider())
                // 注册 JWT 过滤器（在 UsernamePasswordAuthenticationFilter 之前执行）
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
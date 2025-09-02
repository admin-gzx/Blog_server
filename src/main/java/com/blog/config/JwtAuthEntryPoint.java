package com.blog.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT认证入口点
 * 
 * 该类实现了AuthenticationEntryPoint接口，用于处理JWT认证异常。
 * 当用户尝试访问需要认证的资源但未提供有效的JWT令牌时，
 * Spring Security会调用该类的commence方法来处理认证异常。
 */
@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthEntryPoint.class);

    /**
     * 处理认证异常的方法
     * 
     * 当发生认证异常时，该方法会被Spring Security调用。
     * 它会记录未授权访问的日志，并向客户端返回HTTP 401（未授权）状态码和错误信息。
     * 
     * @param request       HTTP请求对象
     * @param response      HTTP响应对象
     * @param authException 认证异常对象
     * @throws IOException      IO异常
     * @throws ServletException Servlet异常
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // 记录未授权错误日志，包含异常信息
        logger.error("Unauthorized error: {}", authException.getMessage());
        
        // 向客户端发送HTTP 401错误响应，表示未授权访问
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
    }

}
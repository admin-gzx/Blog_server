package com.blog.config;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT认证过滤器
 * 用于拦截HTTP请求，验证JWT令牌并设置用户认证信息
 */
public class JwtAuthTokenFilter extends OncePerRequestFilter {

    /** 自动注入JWT工具类 */
    @Autowired
    private JwtUtils jwtUtils;

    /** 自动注入用户详情服务类 */
    @Autowired
    private UserDetailsService userDetailsService;

    /** 日志记录器 */
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthTokenFilter.class);

    /**
     * 过滤器核心方法，用于处理每个HTTP请求
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @param filterChain 过滤器链
     * @throws ServletException Servlet异常
     * @throws IOException IO异常
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 检查请求路径是否为公开接口
        String requestURI = request.getRequestURI();
        
        // 添加调试日志
        logger.debug("Request URI: {}", requestURI);
        
        // 让Spring Security处理路径权限，这里只处理需要认证的请求
        // 如果是公开接口，Spring Security会直接放行，不会执行到这里
        
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 从HTTP请求中解析JWT令牌
     * @param request HTTP请求对象
     * @return JWT令牌字符串，如果不存在则返回null
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }

        return null;
    }
}
package com.blog;

import com.blog.config.JwtUtils;
import com.blog.config.UserDetailsImpl;
import com.blog.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtUtilsTest {

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    public void testJwtTokenGenerationAndValidation() {
        // 创建一个模拟的用户对象
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");
        
        // 创建UserDetailsImpl对象
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        
        // 创建一个模拟的认证对象
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, Collections.emptyList());
        
        // 生成JWT令牌
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        // 验证令牌
        boolean isValid = jwtUtils.validateJwtToken(jwt);
        
        // 令牌应该有效
        assertTrue(isValid);
        
        // 从令牌中解析用户名
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        
        // 用户名应该匹配
        assertEquals("testuser", username);
    }
}
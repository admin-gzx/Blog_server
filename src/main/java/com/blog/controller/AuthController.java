package com.blog.controller;

import com.blog.dto.JwtResponse;
import com.blog.dto.LoginRequest;
import com.blog.dto.UserDto;
import com.blog.entity.User;
import com.blog.service.UserService;
import com.blog.config.JwtUtils;
import com.blog.config.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 * 提供用户登录和注册的RESTful API接口
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "认证接口", description = "用户认证相关接口")
public class AuthController {
    
    /**
     * 自动注入认证管理器
     */
    @Autowired
    AuthenticationManager authenticationManager;
    
    /**
     * 自动注入用户服务类
     */
    @Autowired
    UserService userService;
    
    /**
     * 自动注入密码编码器
     */
    @Autowired
    PasswordEncoder encoder;
    
    /**
     * 自动注入JWT工具类
     */
    @Autowired
    JwtUtils jwtUtils;
    
    /**
     * 用户登录接口
     * @param loginRequest 包含用户名和密码的登录请求对象
     * @return 登录成功后的JWT令牌信息
     */
    @PostMapping("/signin")
    @Operation(summary = "用户登录", description = "用户登录接口")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        // 使用用户名和密码进行身份验证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        
        // 将认证信息存储到安全上下文中
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 生成JWT令牌
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        // 获取认证用户详细信息
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        // 返回JWT响应对象
        return ResponseEntity.ok(new JwtResponse(jwt, 
                userDetails.getId(), 
                userDetails.getUsername(), 
                userDetails.getEmail()));
    }
    
    /**
     * 用户注册接口
     * @param userDto 包含用户注册信息的数据传输对象
     * @return 注册结果信息
     */
    @PostMapping("/signup")
    @Operation(summary = "用户注册", description = "用户注册接口")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto userDto) {
        // 检查用户名是否已被使用
        if (userService.existsByUsername(userDto.getUsername())) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "用户名已被使用");
            return ResponseEntity.badRequest().body(error);
        }
        
        // 检查邮箱是否已被使用
        if (userService.existsByEmail(userDto.getEmail())) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "邮箱已被使用");
            return ResponseEntity.badRequest().body(error);
        }
        
        // 创建新用户
        // 对密码进行加密
        userDto.setPassword(encoder.encode(userDto.getPassword()));
        UserDto newUser = userService.createUser(userDto);
        
        // 返回注册成功信息
        Map<String, String> success = new HashMap<>();
        success.put("message", "用户注册成功");
        return ResponseEntity.ok(success);
    }
}
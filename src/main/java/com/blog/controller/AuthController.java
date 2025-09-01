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

@RestController
@RequestMapping("/api/auth")
@Tag(name = "认证接口", description = "用户认证相关接口")
public class AuthController {
    
    @Autowired
    AuthenticationManager authenticationManager;
    
    @Autowired
    UserService userService;
    
    @Autowired
    PasswordEncoder encoder;
    
    @Autowired
    JwtUtils jwtUtils;
    
    @PostMapping("/signin")
    @Operation(summary = "用户登录", description = "用户登录接口")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        return ResponseEntity.ok(new JwtResponse(jwt, 
                userDetails.getId(), 
                userDetails.getUsername(), 
                userDetails.getEmail()));
    }
    
    @PostMapping("/signup")
    @Operation(summary = "用户注册", description = "用户注册接口")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto userDto) {
        if (userService.existsByUsername(userDto.getUsername())) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "用户名已被使用");
            return ResponseEntity.badRequest().body(error);
        }
        
        if (userService.existsByEmail(userDto.getEmail())) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "邮箱已被使用");
            return ResponseEntity.badRequest().body(error);
        }
        
        // 创建新用户
        userDto.setPassword(encoder.encode(userDto.getPassword()));
        UserDto newUser = userService.createUser(userDto);
        
        Map<String, String> success = new HashMap<>();
        success.put("message", "用户注册成功");
        return ResponseEntity.ok(success);
    }
}
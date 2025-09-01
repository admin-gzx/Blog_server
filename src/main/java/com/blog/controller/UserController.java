package com.blog.controller;

import com.blog.dto.UserDto;
import com.blog.exception.ResourceNotFoundException;
import com.blog.service.UserService;
import com.blog.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "用户管理接口", description = "用户管理相关接口")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping
    @Operation(summary = "创建用户", description = "创建一个新的用户")
    public ResponseEntity<Object> createUser(
            @Parameter(description = "用户信息", required = true) @Valid @RequestBody UserDto userDto) {
        try {
            UserDto createdUser = userService.createUser(userDto);
            return ResponseUtil.buildSuccessResponse(createdUser);
        } catch (Exception e) {
            return ResponseUtil.buildErrorResponse("创建用户失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取用户", description = "根据用户ID获取用户详细信息")
    public ResponseEntity<Object> getUserById(
            @Parameter(description = "用户ID", required = true) @PathVariable Long id) {
        Optional<UserDto> userOptional = userService.getUserById(id);
        if (userOptional.isPresent()) {
            return ResponseUtil.buildSuccessResponse(userOptional.get());
        } else {
            return ResponseUtil.buildNotFoundResponse("用户未找到");
        }
    }
    
    @GetMapping
    @Operation(summary = "获取所有用户", description = "分页获取所有用户列表")
    public ResponseEntity<Object> getAllUsers(
            @Parameter(description = "页码", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<UserDto> users = userService.getAllUsers(pageable);
            return ResponseUtil.buildSuccessResponse(users.getContent());
        } catch (Exception e) {
            return ResponseUtil.buildErrorResponse("获取用户列表失败: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新用户", description = "根据用户ID更新用户信息")
    public ResponseEntity<Object> updateUser(
            @Parameter(description = "用户ID", required = true) @PathVariable Long id,
            @Parameter(description = "用户信息", required = true) @Valid @RequestBody UserDto userDto) {
        try {
            UserDto updatedUser = userService.updateUser(id, userDto);
            return ResponseUtil.buildSuccessResponse(updatedUser);
        } catch (Exception e) {
            return ResponseUtil.buildErrorResponse("更新用户失败: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "根据用户ID删除用户")
    public ResponseEntity<Object> deleteUser(
            @Parameter(description = "用户ID", required = true) @PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseUtil.buildSuccessResponse("用户删除成功");
        } catch (Exception e) {
            return ResponseUtil.buildErrorResponse("删除用户失败: " + e.getMessage());
        }
    }
    
    
    @GetMapping("/exists/username/{username}")
    @Operation(summary = "检查用户名是否存在", description = "检查指定用户名是否已存在")
    public ResponseEntity<Object> existsByUsername(
            @Parameter(description = "用户名", required = true) @PathVariable String username) {
        boolean exists = userService.existsByUsername(username);
        return ResponseUtil.buildSuccessResponse(exists);
    }
    
    @GetMapping("/exists/email/{email}")
    @Operation(summary = "检查邮箱是否存在", description = "检查指定邮箱是否已存在")
    public ResponseEntity<Object> existsByEmail(
            @Parameter(description = "邮箱", required = true) @PathVariable String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseUtil.buildSuccessResponse(exists);
    }
}
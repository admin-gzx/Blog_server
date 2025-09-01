package com.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@Tag(name = "测试接口", description = "测试接口API")
public class TestController {

    @GetMapping("/all")
    @Operation(summary = "公共测试接口", description = "公共测试接口")
    public ResponseEntity<Map<String, String>> allAccess() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "欢迎访问博客系统公共测试接口");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    @Operation(summary = "用户测试接口", description = "用户测试接口")
    public ResponseEntity<Map<String, String>> userAccess() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "欢迎访问博客系统用户测试接口");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin")
    @Operation(summary = "管理员测试接口", description = "管理员测试接口")
    public ResponseEntity<Map<String, String>> adminAccess() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "欢迎访问博客系统管理员测试接口");
        return ResponseEntity.ok(response);
    }
}
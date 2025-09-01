package com.blog.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagDto {
    private Long id;
    
    @NotBlank(message = "标签名称不能为空")
    private String name;
    
    private String description;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
}
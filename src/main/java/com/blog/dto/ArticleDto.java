package com.blog.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {
    private Long id;
    
    @NotBlank(message = "文章标题不能为空")
    private String title;
    
    @NotBlank(message = "文章内容不能为空")
    private String content;
    
    private String summary;
    
    private String coverImage;
    
    private Boolean published;
    
    private Integer viewCount;
    
    private Integer likeCount;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    private UserDto author;
    
    private List<TagDto> tags;
    
    private CategoryDto category;
}
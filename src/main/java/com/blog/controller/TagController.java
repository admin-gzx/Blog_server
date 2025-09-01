package com.blog.controller;

import com.blog.dto.TagDto;
import com.blog.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 标签管理控制器
 * 提供标签管理相关的RESTful API接口
 */
@RestController
@RequestMapping("/api/tags")
@Tag(name = "标签管理", description = "标签管理相关接口")
public class TagController {
    
    /** 自动注入标签服务类 */
    @Autowired
    private TagService tagService;
    
    /**
     * 创建新的标签
     * @param tagDto 包含标签信息的数据传输对象
     * @return 创建成功的标签信息
     */
    @PostMapping
    @Operation(summary = "创建标签", description = "创建新的标签")
    public ResponseEntity<TagDto> createTag(@Valid @RequestBody TagDto tagDto) {
        TagDto createdTag = tagService.createTag(tagDto);
        return ResponseEntity.ok(createdTag);
    }
    
    /**
     * 根据ID获取标签详情
     * @param id 标签的唯一标识符
     * @return 包含标签信息的ResponseEntity对象
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取标签", description = "根据标签ID获取标签详情")
    public ResponseEntity<TagDto> getTagById(@PathVariable Long id) {
        return tagService.getTagById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * 更新指定ID的标签
     * @param id 要更新的标签ID
     * @param tagDto 包含更新信息的数据传输对象
     * @return 更新后的标签信息
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新标签", description = "更新指定ID的标签")
    public ResponseEntity<TagDto> updateTag(@PathVariable Long id, @Valid @RequestBody TagDto tagDto) {
        try {
            TagDto updatedTag = tagService.updateTag(id, tagDto);
            return ResponseEntity.ok(updatedTag);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 删除指定ID的标签
     * @param id 要删除的标签ID
     * @return 删除结果的ResponseEntity对象
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除标签", description = "删除指定ID的标签")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 分页获取所有标签列表
     * @param page 页码（从0开始）
     * @param size 每页大小
     * @return 分页的标签列表
     */
    @GetMapping
    @Operation(summary = "分页获取标签列表", description = "分页获取所有标签列表")
    public ResponseEntity<Page<TagDto>> getAllTags(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TagDto> tags = tagService.getAllTags(pageable);
        return ResponseEntity.ok(tags);
    }
    
    /**
     * 检查指定名称的标签是否存在
     * @param name 标签名称
     * @return 检查结果的ResponseEntity对象
     */
    @GetMapping("/exists/{name}")
    @Operation(summary = "检查标签名称是否存在", description = "检查指定名称的标签是否存在")
    public ResponseEntity<Map<String, Boolean>> existsByName(@PathVariable String name) {
        Boolean exists = tagService.existsByName(name);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
}
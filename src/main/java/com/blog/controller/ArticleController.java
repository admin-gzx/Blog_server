package com.blog.controller;

import com.blog.dto.ArticleDto;
import com.blog.entity.User;
import com.blog.service.ArticleService;
import com.blog.config.UserDetailsImpl;
import com.blog.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 文章管理控制器
 * 提供文章的创建、获取、更新、删除等RESTful API接口
 */
@RestController
@RequestMapping("/api/articles")
@Tag(name = "文章管理", description = "文章管理相关接口")
public class ArticleController {
    
    /**
     * 自动注入文章服务类
     */
    @Autowired
    private ArticleService articleService;
    
    /**
     * 创建新文章
     * @param articleDto 包含文章信息的数据传输对象，必须经过验证
     * @return 创建成功的文章信息
     */
    @PostMapping
    @Operation(summary = "创建文章", description = "创建新的文章")
    public ResponseEntity<?> createArticle(@Valid @RequestBody ArticleDto articleDto) {
        // 检查用户是否已认证
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户未认证");
        }
        
        // 在实际应用中，需要从SecurityContext中获取当前用户
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User author = new User();
        author.setId(userDetails.getId()); // 使用当前登录用户的ID
        try {
            ArticleDto createdArticle = articleService.createArticle(articleDto, author);
            return ResponseEntity.ok(createdArticle);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 根据文章ID获取文章详情
     * @param id 文章的唯一标识符
     * @return 对应ID的文章信息，如果不存在则返回404
     * @throws com.blog.exception.ResourceNotFoundException 如果文章不存在
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取文章", description = "根据文章ID获取文章详情")
    public ResponseEntity<ArticleDto> getArticleById(@PathVariable Long id) throws com.blog.exception.ResourceNotFoundException {
        articleService.incrementViewCount(id); // 增加浏览量
        return articleService.getArticleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * 更新指定ID的文章
     * @param id 要更新的文章ID
     * @param articleDto 包含更新信息的数据传输对象，必须经过验证
     * @return 更新后的文章信息，如果文章不存在则返回404
     * @throws com.blog.exception.ResourceNotFoundException 如果文章不存在
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新文章", description = "更新指定ID的文章")
    public ResponseEntity<ArticleDto> updateArticle(@PathVariable Long id, @Valid @RequestBody ArticleDto articleDto) throws com.blog.exception.ResourceNotFoundException {
        try {
            ArticleDto updatedArticle = articleService.updateArticle(id, articleDto);
            return ResponseEntity.ok(updatedArticle);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 删除指定ID的文章
     * @param id 要删除的文章ID
     * @return 删除成功的响应
     * @throws com.blog.exception.ResourceNotFoundException 如果文章不存在
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除文章", description = "删除指定ID的文章")

    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) throws com.blog.exception.ResourceNotFoundException {
        articleService.deleteArticle(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 分页获取所有文章列表
     * @param page 页码，从0开始，默认为0
     * @param size 每页大小，默认为10
     * @return 分页的文章列表
     */
    @GetMapping
    @Operation(summary = "分页获取所有文章", description = "分页获取所有文章列表")
    public ResponseEntity<Page<ArticleDto>> getAllArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ArticleDto> articles = articleService.getAllArticles(pageable);
        return ResponseEntity.ok(articles);
    }
    
    /**
     * 分页获取已发布的文章列表
     * @param page 页码，从0开始，默认为0
     * @param size 每页大小，默认为10
     * @return 分页的已发布文章列表
     */
    @GetMapping("/published")
    @Operation(summary = "分页获取已发布的文章", description = "分页获取已发布的文章列表")
    public ResponseEntity<Page<ArticleDto>> getPublishedArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ArticleDto> articles = articleService.getPublishedArticles(pageable);
        return ResponseEntity.ok(articles);
    }
    
    /**
     * 根据作者ID分页获取文章列表
     * @param authorId 作者的唯一标识符
     * @param page 页码，从0开始，默认为0
     * @param size 每页大小，默认为10
     * @return 指定作者的分页文章列表
     * @throws com.blog.exception.ResourceNotFoundException 如果作者不存在
     */
    @GetMapping("/author/{authorId}")
    @Operation(summary = "根据作者ID分页获取文章", description = "根据作者ID分页获取文章列表")
    public ResponseEntity<Page<ArticleDto>> getArticlesByAuthor(
            @PathVariable Long authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws com.blog.exception.ResourceNotFoundException {
        Pageable pageable = PageRequest.of(page, size);
        Page<ArticleDto> articles = articleService.getArticlesByAuthor(authorId, pageable);
        return ResponseEntity.ok(articles);
    }
    
    /**
     * 根据分类ID分页获取文章列表
     * @param categoryId 分类的唯一标识符
     * @param page 页码，从0开始，默认为0
     * @param size 每页大小，默认为10
     * @return 指定分类的分页文章列表
     * @throws com.blog.exception.ResourceNotFoundException 如果分类不存在
     */
    @GetMapping("/category/{categoryId}")
    @Operation(summary = "根据分类ID分页获取文章", description = "根据分类ID分页获取文章列表")
    public ResponseEntity<Page<ArticleDto>> getArticlesByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws com.blog.exception.ResourceNotFoundException {
        Pageable pageable = PageRequest.of(page, size);
        Page<ArticleDto> articles = articleService.getArticlesByCategory(categoryId, pageable);
        return ResponseEntity.ok(articles);
    }
    
    /**
     * 根据标签ID分页获取文章列表
     * @param tagId 标签的唯一标识符
     * @param page 页码，从0开始，默认为0
     * @param size 每页大小，默认为10
     * @return 指定标签的分页文章列表
     * @throws com.blog.exception.ResourceNotFoundException 如果标签不存在
     */
    @GetMapping("/tag/{tagId}")
    @Operation(summary = "根据标签ID分页获取文章", description = "根据标签ID分页获取文章列表")
    public ResponseEntity<Page<ArticleDto>> getArticlesByTag(
            @PathVariable Long tagId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws com.blog.exception.ResourceNotFoundException {
        Pageable pageable = PageRequest.of(page, size);
        Page<ArticleDto> articles = articleService.getArticlesByTag(tagId, pageable);
        return ResponseEntity.ok(articles);
    }
    
    /**
     * 分页获取热门文章列表
     * @param page 页码，从0开始，默认为0
     * @param size 每页大小，默认为10
     * @return 分页的热门文章列表
     */
    @GetMapping("/popular")
    @Operation(summary = "分页获取热门文章", description = "分页获取热门文章列表")
    public ResponseEntity<Page<ArticleDto>> getPopularArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ArticleDto> articles = articleService.getPopularArticles(pageable);
        return ResponseEntity.ok(articles);
    }
    
    /**
     * 获取热门文章前5名
     * @return 热门文章列表（最多5篇）
     */
    @GetMapping("/popular/top")
    @Operation(summary = "获取热门文章Top5", description = "获取热门文章前5名")
    public ResponseEntity<List<ArticleDto>> getTopPopularArticles() {
        List<ArticleDto> articles = articleService.getTopPopularArticles();
        return ResponseEntity.ok(articles);
    }
}
package com.blog.controller;

import com.blog.dto.ArticleDto;
import com.blog.entity.User;
import com.blog.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/articles")
@Tag(name = "文章管理", description = "文章管理相关接口")
public class ArticleController {
    
    @Autowired
    private ArticleService articleService;
    
    @PostMapping
    @Operation(summary = "创建文章", description = "创建新的文章")
    public ResponseEntity<ArticleDto> createArticle(@Valid @RequestBody ArticleDto articleDto) {
        // 在实际应用中，需要从SecurityContext中获取当前用户
        User author = new User();
        author.setId(1L); // 示例用户ID
        ArticleDto createdArticle = articleService.createArticle(articleDto, author);
        return ResponseEntity.ok(createdArticle);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取文章", description = "根据文章ID获取文章详情")
    public ResponseEntity<ArticleDto> getArticleById(@PathVariable Long id) {
        articleService.incrementViewCount(id); // 增加浏览量
        return articleService.getArticleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新文章", description = "更新指定ID的文章")
    public ResponseEntity<ArticleDto> updateArticle(@PathVariable Long id, @Valid @RequestBody ArticleDto articleDto) {
        try {
            ArticleDto updatedArticle = articleService.updateArticle(id, articleDto);
            return ResponseEntity.ok(updatedArticle);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "删除文章", description = "删除指定ID的文章")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping
    @Operation(summary = "分页获取所有文章", description = "分页获取所有文章列表")
    public ResponseEntity<Page<ArticleDto>> getAllArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ArticleDto> articles = articleService.getAllArticles(pageable);
        return ResponseEntity.ok(articles);
    }
    
    @GetMapping("/published")
    @Operation(summary = "分页获取已发布的文章", description = "分页获取已发布的文章列表")
    public ResponseEntity<Page<ArticleDto>> getPublishedArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ArticleDto> articles = articleService.getPublishedArticles(pageable);
        return ResponseEntity.ok(articles);
    }
    
    @GetMapping("/author/{authorId}")
    @Operation(summary = "根据作者ID分页获取文章", description = "根据作者ID分页获取文章列表")
    public ResponseEntity<Page<ArticleDto>> getArticlesByAuthor(
            @PathVariable Long authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ArticleDto> articles = articleService.getArticlesByAuthor(authorId, pageable);
        return ResponseEntity.ok(articles);
    }
    
    @GetMapping("/category/{categoryId}")
    @Operation(summary = "根据分类ID分页获取文章", description = "根据分类ID分页获取文章列表")
    public ResponseEntity<Page<ArticleDto>> getArticlesByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ArticleDto> articles = articleService.getArticlesByCategory(categoryId, pageable);
        return ResponseEntity.ok(articles);
    }
    
    @GetMapping("/tag/{tagId}")
    @Operation(summary = "根据标签ID分页获取文章", description = "根据标签ID分页获取文章列表")
    public ResponseEntity<Page<ArticleDto>> getArticlesByTag(
            @PathVariable Long tagId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ArticleDto> articles = articleService.getArticlesByTag(tagId, pageable);
        return ResponseEntity.ok(articles);
    }
    
    @GetMapping("/popular")
    @Operation(summary = "分页获取热门文章", description = "分页获取热门文章列表")
    public ResponseEntity<Page<ArticleDto>> getPopularArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ArticleDto> articles = articleService.getPopularArticles(pageable);
        return ResponseEntity.ok(articles);
    }
    
    @GetMapping("/popular/top")
    @Operation(summary = "获取热门文章Top5", description = "获取热门文章前5名")
    public ResponseEntity<List<ArticleDto>> getTopPopularArticles() {
        List<ArticleDto> articles = articleService.getTopPopularArticles();
        return ResponseEntity.ok(articles);
    }
}
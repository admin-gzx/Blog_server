package com.blog.service;

import com.blog.dto.ArticleDto;
import com.blog.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ArticleService {
    ArticleDto createArticle(ArticleDto articleDto, User author);
    Optional<ArticleDto> getArticleById(Long id);
    ArticleDto updateArticle(Long id, ArticleDto articleDto);
    void deleteArticle(Long id);
    Page<ArticleDto> getAllArticles(Pageable pageable);
    Page<ArticleDto> getPublishedArticles(Pageable pageable);
    Page<ArticleDto> getArticlesByAuthor(Long authorId, Pageable pageable);
    Page<ArticleDto> getArticlesByCategory(Long categoryId, Pageable pageable);
    Page<ArticleDto> getArticlesByTag(Long tagId, Pageable pageable);
    Page<ArticleDto> getPopularArticles(Pageable pageable);
    List<ArticleDto> getTopPopularArticles();
    void incrementViewCount(Long id);
}
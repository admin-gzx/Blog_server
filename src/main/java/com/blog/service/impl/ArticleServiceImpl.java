package com.blog.service.impl;

import com.blog.dto.ArticleDto;
import com.blog.entity.Article;
import com.blog.entity.Category;
import com.blog.entity.Tag;
import com.blog.entity.User;
import com.blog.repository.ArticleRepository;
import com.blog.repository.CategoryRepository;
import com.blog.repository.TagRepository;
import com.blog.repository.UserRepository;
import com.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {
    
    @Autowired
    private ArticleRepository articleRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private TagRepository tagRepository;
    
    @Override
    public ArticleDto createArticle(ArticleDto articleDto, User author) {
        Article article = new Article();
        article.setTitle(articleDto.getTitle());
        article.setContent(articleDto.getContent());
        article.setSummary(articleDto.getSummary());
        article.setCoverImage(articleDto.getCoverImage());
        article.setPublished(articleDto.getPublished());
        article.setAuthor(author);
        
        Article savedArticle = articleRepository.save(article);
        return convertToDto(savedArticle);
    }
    
    @Override
    public Optional<ArticleDto> getArticleById(Long id) {
        return articleRepository.findById(id).map(this::convertToDto);
    }
    
    @Override
    public ArticleDto updateArticle(Long id, ArticleDto articleDto) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new RuntimeException("Article not found"));
        article.setTitle(articleDto.getTitle());
        article.setContent(articleDto.getContent());
        article.setSummary(articleDto.getSummary());
        article.setCoverImage(articleDto.getCoverImage());
        article.setPublished(articleDto.getPublished());
        
        Article updatedArticle = articleRepository.save(article);
        return convertToDto(updatedArticle);
    }
    
    @Override
    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }
    
    @Override
    public Page<ArticleDto> getAllArticles(Pageable pageable) {
        return articleRepository.findAll(pageable).map(this::convertToDto);
    }
    
    @Override
    public Page<ArticleDto> getPublishedArticles(Pageable pageable) {
        return articleRepository.findByPublishedTrueOrderByCreateTimeDesc(pageable).map(this::convertToDto);
    }
    
    @Override
    public Page<ArticleDto> getArticlesByAuthor(Long authorId, Pageable pageable) {
        User author = userRepository.findById(authorId).orElseThrow(() -> new RuntimeException("User not found"));
        return articleRepository.findByAuthorAndPublishedTrueOrderByCreateTimeDesc(author, pageable).map(this::convertToDto);
    }
    
    @Override
    public Page<ArticleDto> getArticlesByCategory(Long categoryId, Pageable pageable) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));
        return articleRepository.findByCategoryAndPublishedTrueOrderByCreateTimeDesc(category, pageable).map(this::convertToDto);
    }
    
    @Override
    public Page<ArticleDto> getArticlesByTag(Long tagId, Pageable pageable) {
        Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new RuntimeException("Tag not found"));
        return articleRepository.findByTagsContainingAndPublishedTrueOrderByCreateTimeDesc(tag, pageable).map(this::convertToDto);
    }
    
    @Override
    public Page<ArticleDto> getPopularArticles(Pageable pageable) {
        return articleRepository.findPopularArticles(pageable).map(this::convertToDto);
    }
    
    @Override
    public List<ArticleDto> getTopPopularArticles() {
        return articleRepository.findTop5ByPublishedTrueOrderByViewCountDesc()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public void incrementViewCount(Long id) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new RuntimeException("Article not found"));
        article.setViewCount(article.getViewCount() + 1);
        articleRepository.save(article);
    }
    
    private ArticleDto convertToDto(Article article) {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setId(article.getId());
        articleDto.setTitle(article.getTitle());
        articleDto.setContent(article.getContent());
        articleDto.setSummary(article.getSummary());
        articleDto.setCoverImage(article.getCoverImage());
        articleDto.setPublished(article.getPublished());
        articleDto.setViewCount(article.getViewCount());
        articleDto.setLikeCount(article.getLikeCount());
        articleDto.setCreateTime(article.getCreateTime());
        articleDto.setUpdateTime(article.getUpdateTime());
        
        // 转换作者信息
        if (article.getAuthor() != null) {
            User author = article.getAuthor();
            articleDto.setAuthor(convertUserToDto(author));
        }
        
        return articleDto;
    }
    
    private com.blog.dto.UserDto convertUserToDto(User user) {
        com.blog.dto.UserDto userDto = new com.blog.dto.UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setNickname(user.getNickname());
        userDto.setAvatar(user.getAvatar());
        userDto.setEnabled(user.getEnabled());
        userDto.setCreateTime(user.getCreateTime());
        userDto.setUpdateTime(user.getUpdateTime());
        return userDto;
    }
}
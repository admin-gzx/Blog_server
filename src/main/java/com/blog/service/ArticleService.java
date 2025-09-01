package com.blog.service;

import com.blog.dto.ArticleDto;
import com.blog.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 文章服务接口
 * 定义了文章管理相关业务逻辑的方法
 */
public interface ArticleService {
    /**
     * 创建新文章
     * @param articleDto 包含文章信息的数据传输对象
     * @param author 文章作者
     * @return 创建成功的文章信息
     */
    ArticleDto createArticle(ArticleDto articleDto, User author);
    
    /**
     * 根据ID获取文章
     * @param id 文章的唯一标识符
     * @return 包含文章信息的Optional对象
     */
    Optional<ArticleDto> getArticleById(Long id);
    
    /**
     * 更新指定ID的文章
     * @param id 要更新的文章ID
     * @param articleDto 包含更新信息的数据传输对象
     * @return 更新后的文章信息
     */
    ArticleDto updateArticle(Long id, ArticleDto articleDto);
    
    /**
     * 删除指定ID的文章
     * @param id 要删除的文章ID
     */
    void deleteArticle(Long id);
    
    /**
     * 分页获取所有文章
     * @param pageable 分页信息
     * @return 分页的文章列表
     */
    Page<ArticleDto> getAllArticles(Pageable pageable);
    
    /**
     * 分页获取已发布的文章
     * @param pageable 分页信息
     * @return 分页的已发布文章列表
     */
    Page<ArticleDto> getPublishedArticles(Pageable pageable);
    
    /**
     * 根据作者ID分页获取文章
     * @param authorId 作者的唯一标识符
     * @param pageable 分页信息
     * @return 指定作者的分页文章列表
     */
    Page<ArticleDto> getArticlesByAuthor(Long authorId, Pageable pageable);
    
    /**
     * 根据分类ID分页获取文章
     * @param categoryId 分类的唯一标识符
     * @param pageable 分页信息
     * @return 指定分类的分页文章列表
     */
    Page<ArticleDto> getArticlesByCategory(Long categoryId, Pageable pageable);
    
    /**
     * 根据标签ID分页获取文章
     * @param tagId 标签的唯一标识符
     * @param pageable 分页信息
     * @return 指定标签的分页文章列表
     */
    Page<ArticleDto> getArticlesByTag(Long tagId, Pageable pageable);
    
    /**
     * 分页获取热门文章
     * @param pageable 分页信息
     * @return 分页的热门文章列表
     */
    Page<ArticleDto> getPopularArticles(Pageable pageable);
    
    /**
     * 获取热门文章Top5
     * @return 热门文章列表（最多5篇）
     */
    List<ArticleDto> getTopPopularArticles();
    
    /**
     * 增加文章浏览量
     * @param id 文章的唯一标识符
     */
    void incrementViewCount(Long id);
}
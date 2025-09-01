package com.blog.service.impl;

import com.blog.dto.ArticleDto;
import com.blog.dto.UserDto;
import com.blog.dto.CategoryDto;
import com.blog.dto.TagDto;
import com.blog.entity.Article;
import com.blog.entity.User;
import com.blog.entity.Category;
import com.blog.entity.Tag;
import com.blog.exception.ResourceNotFoundException;
import com.blog.repository.ArticleRepository;
import com.blog.repository.UserRepository;
import com.blog.repository.CategoryRepository;
import com.blog.repository.TagRepository;
import com.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 文章服务实现类
 * 实现了ArticleService接口中定义的所有方法，提供文章管理的具体业务逻辑
 */
@Service
public class ArticleServiceImpl implements ArticleService {
    
    /**
     * 自动注入文章数据访问层
     */
    @Autowired
    private ArticleRepository articleRepository;
    
    /**
     * 自动注入用户数据访问层
     */
    @Autowired
    private UserRepository userRepository;
    
    /**
     * 自动注入分类数据访问层
     */
    @Autowired
    private CategoryRepository categoryRepository;
    
    /**
     * 自动注入标签数据访问层
     */
    @Autowired
    private TagRepository tagRepository;
    
    /**
     * 创建新文章
     * @param articleDto 包含文章信息的数据传输对象
     * @param author 文章作者
     * @return 创建成功的文章信息
     * @throws com.blog.exception.ResourceNotFoundException 如果分类或标签不存在
     */
    @Override
    public ArticleDto createArticle(ArticleDto articleDto, User author) throws com.blog.exception.ResourceNotFoundException {
        // 创建文章实体对象并设置属性
        Article article = new Article();
        article.setTitle(articleDto.getTitle());
        article.setContent(articleDto.getContent());
        article.setSummary(articleDto.getSummary());
        article.setCoverImage(articleDto.getCoverImage());
        article.setPublished(articleDto.getPublished());
        article.setAuthor(author);
        
        // 处理分类关联
        if (articleDto.getCategory() != null && articleDto.getCategory().getId() != null) {
            Category category = categoryRepository.findById(articleDto.getCategory().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + articleDto.getCategory().getId()));
            article.setCategory(category);
        }
        
        // 处理标签关联
        if (articleDto.getTags() != null && !articleDto.getTags().isEmpty()) {
            List<Tag> tags = new ArrayList<>();
            for (TagDto tagDto : articleDto.getTags()) {
                if (tagDto.getId() != null) {
                    try {
                        Tag tag = tagRepository.findById(tagDto.getId())
                                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + tagDto.getId()));
                        tags.add(tag);
                    } catch (ResourceNotFoundException e) {
                        throw e; // 重新抛出异常
                    }
                }
            }
            article.setTags(tags);
        }
        
        // 保存文章到数据库
        Article savedArticle = articleRepository.save(article);
        // 转换为DTO并返回
        return convertToDto(savedArticle);
    }
    
    /**
     * 根据ID获取文章
     * @param id 文章的唯一标识符
     * @return 包含文章信息的Optional对象
     */
    @Override
    public Optional<ArticleDto> getArticleById(Long id) {
        // 从数据库查找文章并转换为DTO
        return articleRepository.findById(id).map(this::convertToDto);
    }
    
    /**
     * 更新指定ID的文章
     * @param id 要更新的文章ID
     * @param articleDto 包含更新信息的数据传输对象
     * @return 更新后的文章信息
     * @throws com.blog.exception.ResourceNotFoundException 如果文章、分类或标签不存在
     */
    @Override
    public ArticleDto updateArticle(Long id, ArticleDto articleDto) throws com.blog.exception.ResourceNotFoundException {
        // 查找要更新的文章
        Article article = articleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        // 更新文章属性
        article.setTitle(articleDto.getTitle());
        article.setContent(articleDto.getContent());
        article.setSummary(articleDto.getSummary());
        article.setCoverImage(articleDto.getCoverImage());
        article.setPublished(articleDto.getPublished());
        
        // 处理分类关联
        if (articleDto.getCategory() != null && articleDto.getCategory().getId() != null) {
            Category category = categoryRepository.findById(articleDto.getCategory().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            article.setCategory(category);
        } else {
            article.setCategory(null);
        }
        
        // 处理标签关联
        if (articleDto.getTags() != null && !articleDto.getTags().isEmpty()) {
            List<Tag> tags = new ArrayList<>();
            for (TagDto tagDto : articleDto.getTags()) {
                if (tagDto.getId() != null) {
                    try {
                        Tag tag = tagRepository.findById(tagDto.getId())
                                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + tagDto.getId()));
                        tags.add(tag);
                    } catch (ResourceNotFoundException e) {
                        throw e; // 重新抛出异常
                    }
                }
            }
            article.setTags(tags);
        } else {
            article.setTags(null);
        }
        
        // 保存更新后的文章
        Article updatedArticle = articleRepository.save(article);
        // 转换为DTO并返回
        return convertToDto(updatedArticle);
    }
    
    /**
     * 删除指定ID的文章
     * @param id 要删除的文章ID
     */
    @Override
    public void deleteArticle(Long id) {
        // 从数据库删除文章
        articleRepository.deleteById(id);
    }
    
    /**
     * 分页获取所有文章
     * @param pageable 分页信息
     * @return 分页的文章列表
     */
    @Override
    public Page<ArticleDto> getAllArticles(Pageable pageable) {
        // 获取所有文章并转换为DTO分页对象
        return articleRepository.findAll(pageable).map(this::convertToDto);
    }
    
    /**
     * 分页获取已发布的文章
     * @param pageable 分页信息
     * @return 分页的已发布文章列表
     */
    @Override
    public Page<ArticleDto> getPublishedArticles(Pageable pageable) {
        // 获取已发布的文章并按创建时间倒序排列，转换为DTO分页对象
        return articleRepository.findByPublishedTrueOrderByCreateTimeDesc(pageable).map(this::convertToDto);
    }
    
    /**
     * 根据作者ID分页获取文章
     * @param authorId 作者的唯一标识符
     * @param pageable 分页信息
     * @return 指定作者的分页文章列表
     * @throws com.blog.exception.ResourceNotFoundException 如果作者不存在
     */
    @Override
    public Page<ArticleDto> getArticlesByAuthor(Long authorId, Pageable pageable) throws com.blog.exception.ResourceNotFoundException {
        // 查找作者
        User author = userRepository.findById(authorId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        // 获取指定作者的已发布文章并按创建时间倒序排列，转换为DTO分页对象
        return articleRepository.findByAuthorAndPublishedTrueOrderByCreateTimeDesc(author, pageable).map(this::convertToDto);
    }
    
    /**
     * 根据分类ID分页获取文章
     * @param categoryId 分类的唯一标识符
     * @param pageable 分页信息
     * @return 指定分类的分页文章列表
     * @throws com.blog.exception.ResourceNotFoundException 如果分类不存在
     */
    @Override
    public Page<ArticleDto> getArticlesByCategory(Long categoryId, Pageable pageable) throws com.blog.exception.ResourceNotFoundException {
        // 查找分类
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        // 获取指定分类的已发布文章并按创建时间倒序排列，转换为DTO分页对象
        return articleRepository.findByCategoryAndPublishedTrueOrderByCreateTimeDesc(category, pageable).map(this::convertToDto);
    }
    
    /**
     * 根据标签ID分页获取文章
     * @param tagId 标签的唯一标识符
     * @param pageable 分页信息
     * @return 指定标签的分页文章列表
     * @throws com.blog.exception.ResourceNotFoundException 如果标签不存在
     */
    @Override
    public Page<ArticleDto> getArticlesByTag(Long tagId, Pageable pageable) throws com.blog.exception.ResourceNotFoundException {
        // 查找标签
        Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
        // 获取包含指定标签的已发布文章并按创建时间倒序排列，转换为DTO分页对象
        return articleRepository.findByTagsContainingAndPublishedTrueOrderByCreateTimeDesc(tag, pageable).map(this::convertToDto);
    }
    
    /**
     * 分页获取热门文章
     * @param pageable 分页信息
     * @return 分页的热门文章列表
     */
    @Override
    public Page<ArticleDto> getPopularArticles(Pageable pageable) {
        // 获取热门文章并转换为DTO分页对象
        return articleRepository.findPopularArticles(pageable).map(this::convertToDto);
    }
    
    /**
     * 获取热门文章Top5
     * @return 热门文章列表（最多5篇）
     */
    @Override
    public List<ArticleDto> getTopPopularArticles() {
        // 获取浏览量前5的已发布文章，转换为DTO列表
        return articleRepository.findTop5ByPublishedTrueOrderByViewCountDesc()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * 增加文章浏览量
     * @param id 文章的唯一标识符
     * @throws com.blog.exception.ResourceNotFoundException 如果文章不存在
     */
    @Override
    public void incrementViewCount(Long id) throws com.blog.exception.ResourceNotFoundException {
        // 查找文章
        Article article = articleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        // 增加浏览量
        article.setViewCount(article.getViewCount() + 1);
        // 保存更新后的文章
        articleRepository.save(article);
    }
    
    /**
     * 将文章实体转换为DTO对象
     * @param article 文章实体
     * @return 文章DTO对象
     */
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
        
        // 转换分类信息
        if (article.getCategory() != null) {
            Category category = article.getCategory();
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(category.getId());
            categoryDto.setName(category.getName());
            categoryDto.setDescription(category.getDescription());
            articleDto.setCategory(categoryDto);
        }
        
        // 转换标签信息
        if (article.getTags() != null) {
            List<TagDto> tagDtos = article.getTags().stream().map(tag -> {
                TagDto tagDto = new TagDto();
                tagDto.setId(tag.getId());
                tagDto.setName(tag.getName());
                return tagDto;
            }).collect(Collectors.toList());
            articleDto.setTags(tagDtos);
        }
        
        return articleDto;
    }
    
    /**
     * 将用户实体转换为DTO对象
     * @param user 用户实体
     * @return 用户DTO对象
     */
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
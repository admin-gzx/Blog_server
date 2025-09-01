package com.blog.service;

import com.blog.dto.TagDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * 标签服务接口
 * 定义了标签管理相关业务逻辑的方法
 */
public interface TagService {
    /**
     * 创建新的标签
     * @param tagDto 包含标签信息的数据传输对象
     * @return 创建成功的标签信息
     */
    TagDto createTag(TagDto tagDto);
    
    /**
     * 根据ID获取标签
     * @param id 标签的唯一标识符
     * @return 包含标签信息的Optional对象
     */
    Optional<TagDto> getTagById(Long id);
    
    /**
     * 根据名称获取标签
     * @param name 标签名称
     * @return 包含标签信息的Optional对象
     */
    Optional<TagDto> getTagByName(String name);
    
    /**
     * 更新指定ID的标签
     * @param id 要更新的标签ID
     * @param tagDto 包含更新信息的数据传输对象
     * @return 更新后的标签信息
     */
    TagDto updateTag(Long id, TagDto tagDto);
    
    /**
     * 删除指定ID的标签
     * @param id 要删除的标签ID
     */
    void deleteTag(Long id);
    
    /**
     * 分页获取所有标签
     * @param pageable 分页信息
     * @return 分页的标签列表
     */
    Page<TagDto> getAllTags(Pageable pageable);
    
    /**
     * 检查标签名称是否存在
     * @param name 标签名称
     * @return 标签名称存在返回true，否则返回false
     */
    Boolean existsByName(String name);
}
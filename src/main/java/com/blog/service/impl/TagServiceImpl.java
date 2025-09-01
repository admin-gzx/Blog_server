package com.blog.service.impl;

import com.blog.dto.TagDto;
import com.blog.entity.Tag;
import com.blog.repository.TagRepository;
import com.blog.exception.ResourceNotFoundException;
import com.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 标签服务实现类
 * 实现了TagService接口中定义的标签管理业务逻辑
 */
@Service
public class TagServiceImpl implements TagService {
    
    /** 自动注入标签数据访问层 */
    @Autowired
    private TagRepository tagRepository;
    
    /**
     * 创建新的标签
     * @param tagDto 包含标签信息的数据传输对象
     * @return 创建成功的标签信息
     */
    @Override
    public TagDto createTag(TagDto tagDto) {
        // 创建标签实体对象
        Tag tag = new Tag();
        tag.setName(tagDto.getName());
        tag.setDescription(tagDto.getDescription());
        
        // 保存标签信息到数据库
        Tag savedTag = tagRepository.save(tag);
        // 转换为DTO对象并返回
        return convertToDto(savedTag);
    }
    
    /**
     * 根据ID获取标签
     * @param id 标签的唯一标识符
     * @return 包含标签信息的Optional对象
     */
    @Override
    public Optional<TagDto> getTagById(Long id) {
        // 根据ID从数据库查找标签，并转换为DTO对象
        return tagRepository.findById(id).map(this::convertToDto);
    }
    
    /**
     * 根据名称获取标签
     * @param name 标签名称
     * @return 包含标签信息的Optional对象
     */
    @Override
    public Optional<TagDto> getTagByName(String name) {
        // 根据名称从数据库查找标签，并转换为DTO对象
        return tagRepository.findByName(name).map(this::convertToDto);
    }
    
    /**
     * 更新指定ID的标签
     * @param id 要更新的标签ID
     * @param tagDto 包含更新信息的数据传输对象
     * @return 更新后的标签信息
     * @throws ResourceNotFoundException 如果标签不存在
     */
    @Override
    public TagDto updateTag(Long id, TagDto tagDto) throws ResourceNotFoundException {
        // 根据ID查找标签，如果不存在则抛出异常
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
        tag.setName(tagDto.getName());
        tag.setDescription(tagDto.getDescription());
        
        // 保存更新后的标签信息到数据库
        Tag updatedTag = tagRepository.save(tag);
        // 转换为DTO对象并返回
        return convertToDto(updatedTag);
    }
    
    /**
     * 删除指定ID的标签
     * @param id 要删除的标签ID
     */
    @Override
    public void deleteTag(Long id) {
        // 根据ID删除标签
        tagRepository.deleteById(id);
    }
    
    /**
     * 分页获取所有标签
     * @param pageable 分页信息
     * @return 分页的标签列表
     */
    @Override
    public Page<TagDto> getAllTags(Pageable pageable) {
        // 从数据库分页获取所有标签，并转换为DTO对象
        return tagRepository.findAll(pageable).map(this::convertToDto);
    }
    
    /**
     * 检查标签名称是否存在
     * @param name 标签名称
     * @return 标签名称存在返回true，否则返回false
     */
    @Override
    public Boolean existsByName(String name) {
        // 检查数据库中是否存在指定名称的标签
        return tagRepository.existsByName(name);
    }
    
    /**
     * 将标签实体对象转换为DTO对象
     * @param tag 标签实体对象
     * @return 标签DTO对象
     */
    private TagDto convertToDto(Tag tag) {
        TagDto tagDto = new TagDto();
        tagDto.setId(tag.getId());
        tagDto.setName(tag.getName());
        tagDto.setDescription(tag.getDescription());
        tagDto.setCreateTime(tag.getCreateTime());
        tagDto.setUpdateTime(tag.getUpdateTime());
        return tagDto;
    }
}
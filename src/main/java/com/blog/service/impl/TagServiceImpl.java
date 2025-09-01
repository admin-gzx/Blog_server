package com.blog.service.impl;

import com.blog.dto.TagDto;
import com.blog.entity.Tag;
import com.blog.repository.TagRepository;
import com.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    
    @Autowired
    private TagRepository tagRepository;
    
    @Override
    public TagDto createTag(TagDto tagDto) {
        Tag tag = new Tag();
        tag.setName(tagDto.getName());
        tag.setDescription(tagDto.getDescription());
        
        Tag savedTag = tagRepository.save(tag);
        return convertToDto(savedTag);
    }
    
    @Override
    public Optional<TagDto> getTagById(Long id) {
        return tagRepository.findById(id).map(this::convertToDto);
    }
    
    @Override
    public Optional<TagDto> getTagByName(String name) {
        return tagRepository.findByName(name).map(this::convertToDto);
    }
    
    @Override
    public TagDto updateTag(Long id, TagDto tagDto) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new RuntimeException("Tag not found"));
        tag.setName(tagDto.getName());
        tag.setDescription(tagDto.getDescription());
        
        Tag updatedTag = tagRepository.save(tag);
        return convertToDto(updatedTag);
    }
    
    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }
    
    @Override
    public Page<TagDto> getAllTags(Pageable pageable) {
        return tagRepository.findAll(pageable).map(this::convertToDto);
    }
    
    @Override
    public Boolean existsByName(String name) {
        return tagRepository.existsByName(name);
    }
    
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
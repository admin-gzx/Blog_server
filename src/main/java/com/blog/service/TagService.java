package com.blog.service;

import com.blog.dto.TagDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TagService {
    TagDto createTag(TagDto tagDto);
    Optional<TagDto> getTagById(Long id);
    Optional<TagDto> getTagByName(String name);
    TagDto updateTag(Long id, TagDto tagDto);
    void deleteTag(Long id);
    Page<TagDto> getAllTags(Pageable pageable);
    Boolean existsByName(String name);
}
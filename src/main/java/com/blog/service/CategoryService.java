package com.blog.service;

import com.blog.dto.CategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);
    Optional<CategoryDto> getCategoryById(Long id);
    Optional<CategoryDto> getCategoryByName(String name);
    CategoryDto updateCategory(Long id, CategoryDto categoryDto);
    void deleteCategory(Long id);
    Page<CategoryDto> getAllCategories(Pageable pageable);
    Boolean existsByName(String name);
}
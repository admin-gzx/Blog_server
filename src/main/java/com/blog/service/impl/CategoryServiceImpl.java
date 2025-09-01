package com.blog.service.impl;

import com.blog.dto.CategoryDto;
import com.blog.entity.Category;
import com.blog.repository.CategoryRepository;
import com.blog.exception.ResourceNotFoundException;
import com.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 分类服务实现类
 * 实现了CategoryService接口中定义的分类管理业务逻辑
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    
    /** 自动注入分类数据访问层 */
    @Autowired
    private CategoryRepository categoryRepository;
    
    /**
     * 创建新的分类
     * @param categoryDto 包含分类信息的数据传输对象
     * @return 创建成功的分类信息
     */
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        // 创建分类实体对象
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        
        // 保存分类信息到数据库
        Category savedCategory = categoryRepository.save(category);
        // 转换为DTO对象并返回
        return convertToDto(savedCategory);
    }
    
    /**
     * 根据ID获取分类
     * @param id 分类的唯一标识符
     * @return 包含分类信息的Optional对象
     */
    @Override
    public Optional<CategoryDto> getCategoryById(Long id) {
        // 根据ID从数据库查找分类，并转换为DTO对象
        return categoryRepository.findById(id).map(this::convertToDto);
    }
    
    /**
     * 根据名称获取分类
     * @param name 分类名称
     * @return 包含分类信息的Optional对象
     */
    @Override
    public Optional<CategoryDto> getCategoryByName(String name) {
        // 根据名称从数据库查找分类，并转换为DTO对象
        return categoryRepository.findByName(name).map(this::convertToDto);
    }
    
    /**
     * 更新指定ID的分类
     * @param id 要更新的分类ID
     * @param categoryDto 包含更新信息的数据传输对象
     * @return 更新后的分类信息
     */
    @Override
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        // 根据ID查找分类，如果不存在则抛出异常
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        
        // 保存更新后的分类信息到数据库
        Category updatedCategory = categoryRepository.save(category);
        // 转换为DTO对象并返回
        return convertToDto(updatedCategory);
    }
    
    /**
     * 删除指定ID的分类
     * @param id 要删除的分类ID
     */
    @Override
    public void deleteCategory(Long id) {
        // 根据ID删除分类
        categoryRepository.deleteById(id);
    }
    
    /**
     * 分页获取所有分类
     * @param pageable 分页信息
     * @return 分页的分类列表
     */
    @Override
    public Page<CategoryDto> getAllCategories(Pageable pageable) {
        // 从数据库分页获取所有分类，并转换为DTO对象
        return categoryRepository.findAll(pageable).map(this::convertToDto);
    }
    
    /**
     * 检查分类名称是否存在
     * @param name 分类名称
     * @return 分类名称存在返回true，否则返回false
     */
    @Override
    public Boolean existsByName(String name) {
        // 检查数据库中是否存在指定名称的分类
        return categoryRepository.existsByName(name);
    }
    
    /**
     * 将分类实体对象转换为DTO对象
     * @param category 分类实体对象
     * @return 分类DTO对象
     */
    private CategoryDto convertToDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());
        categoryDto.setCreateTime(category.getCreateTime());
        categoryDto.setUpdateTime(category.getUpdateTime());
        return categoryDto;
    }
}
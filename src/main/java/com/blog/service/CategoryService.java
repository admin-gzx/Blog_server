package com.blog.service;

import com.blog.dto.CategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * 分类服务接口
 * 定义了分类管理相关业务逻辑的方法
 */
public interface CategoryService {
    /**
     * 创建新的分类
     * @param categoryDto 包含分类信息的数据传输对象
     * @return 创建成功的分类信息
     */
    CategoryDto createCategory(CategoryDto categoryDto);
    
    /**
     * 根据ID获取分类
     * @param id 分类的唯一标识符
     * @return 包含分类信息的Optional对象
     */
    Optional<CategoryDto> getCategoryById(Long id);
    
    /**
     * 根据名称获取分类
     * @param name 分类名称
     * @return 包含分类信息的Optional对象
     */
    Optional<CategoryDto> getCategoryByName(String name);
    
    /**
     * 更新指定ID的分类
     * @param id 要更新的分类ID
     * @param categoryDto 包含更新信息的数据传输对象
     * @return 更新后的分类信息
     * @throws com.blog.exception.ResourceNotFoundException 如果分类不存在
     */
    CategoryDto updateCategory(Long id, CategoryDto categoryDto) throws com.blog.exception.ResourceNotFoundException;
    
    /**
     * 删除指定ID的分类
     * @param id 要删除的分类ID
     */
    void deleteCategory(Long id);
    
    /**
     * 分页获取所有分类
     * @param pageable 分页信息
     * @return 分页的分类列表
     */
    Page<CategoryDto> getAllCategories(Pageable pageable);
    
    /**
     * 检查分类名称是否存在
     * @param name 分类名称
     * @return 分类名称存在返回true，否则返回false
     */
    Boolean existsByName(String name);
}
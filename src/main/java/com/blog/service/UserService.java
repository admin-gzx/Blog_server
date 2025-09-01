package com.blog.service;

import com.blog.dto.UserDto;
import com.blog.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.Optional;

/**
 * 用户服务接口
 * 定义了用户管理相关业务逻辑的方法
 */
public interface UserService {
    /**
     * 创建新用户
     * @param userDto 包含用户信息的数据传输对象
     * @return 创建成功的用户信息
     */
    UserDto createUser(UserDto userDto);
    
    /**
     * 根据ID获取用户
     * @param id 用户的唯一标识符
     * @return 包含用户信息的Optional对象
     */
    Optional<UserDto> getUserById(Long id);
    
    /**
     * 根据用户名获取用户
     * @param username 用户名
     * @return 包含用户信息的Optional对象
     */
    Optional<UserDto> getUserByUsername(String username);
    
    /**
     * 更新指定ID的用户
     * @param id 要更新的用户ID
     * @param userDto 包含更新信息的数据传输对象
     * @return 更新后的用户信息
     * @throws com.blog.exception.ResourceNotFoundException 如果用户不存在
     */
    UserDto updateUser(Long id, UserDto userDto) throws com.blog.exception.ResourceNotFoundException;
    
    /**
     * 删除指定ID的用户
     * @param id 要删除的用户ID
     */
    void deleteUser(Long id);
    
    /**
     * 分页获取所有用户
     * @param pageable 分页信息
     * @return 分页的用户列表
     */
    Page<UserDto> getAllUsers(Pageable pageable);
    
    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return 用户名存在返回true，否则返回false
     */
    Boolean existsByUsername(String username);
    
    /**
     * 检查邮箱是否存在
     * @param email 邮箱
     * @return 邮箱存在返回true，否则返回false
     */
    Boolean existsByEmail(String email);
}
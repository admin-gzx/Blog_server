package com.blog.service.impl;

import com.blog.dto.UserDto;
import com.blog.entity.User;
import com.blog.repository.UserRepository;
import com.blog.exception.ResourceNotFoundException;
import com.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 * 实现了UserService接口中定义的用户管理业务逻辑
 */
@Service
public class UserServiceImpl implements UserService {
    
    /** 自动注入用户数据访问层 */
    @Autowired
    private UserRepository userRepository;
    
    /** 自动注入密码编码器，用于加密用户密码 */
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * 创建新用户
     * @param userDto 包含用户信息的数据传输对象
     * @return 创建成功的用户信息
     */
    @Override
    public UserDto createUser(UserDto userDto) {
        // 创建用户实体对象
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setNickname(userDto.getNickname());
        user.setAvatar(userDto.getAvatar());
        // 对用户密码进行加密处理
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        
        // 保存用户信息到数据库
        User savedUser = userRepository.save(user);
        // 转换为DTO对象并返回
        return convertToDto(savedUser);
    }
    
    /**
     * 根据ID获取用户
     * @param id 用户的唯一标识符
     * @return 包含用户信息的Optional对象
     */
    @Override
    public Optional<UserDto> getUserById(Long id) {
        // 根据ID从数据库查找用户，并转换为DTO对象
        return userRepository.findById(id).map(this::convertToDto);
    }
    
    /**
     * 根据用户名获取用户
     * @param username 用户名
     * @return 包含用户信息的Optional对象
     */
    @Override
    public Optional<UserDto> getUserByUsername(String username) {
        // 根据用户名从数据库查找用户，并转换为DTO对象
        return userRepository.findByUsername(username).map(this::convertToDto);
    }
    
    /**
     * 更新指定ID的用户
     * @param id 要更新的用户ID
     * @param userDto 包含更新信息的数据传输对象
     * @return 更新后的用户信息
     * @throws ResourceNotFoundException 如果用户不存在
     */
    @Override
    public UserDto updateUser(Long id, UserDto userDto) throws ResourceNotFoundException {
        // 根据ID查找用户，如果不存在则抛出异常
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        // 更新用户昵称和头像
        user.setNickname(userDto.getNickname());
        user.setAvatar(userDto.getAvatar());
        
        // 保存更新后的用户信息到数据库
        User updatedUser = userRepository.save(user);
        // 转换为DTO对象并返回
        return convertToDto(updatedUser);
    }
    
    /**
     * 删除指定ID的用户
     * @param id 要删除的用户ID
     */
    @Override
    public void deleteUser(Long id) {
        // 根据ID删除用户
        userRepository.deleteById(id);
    }
    
    /**
     * 分页获取所有用户
     * @param pageable 分页信息
     * @return 分页的用户列表
     */
    @Override
    public Page<UserDto> getAllUsers(Pageable pageable) {
        // 从数据库分页获取所有用户，并转换为DTO对象
        return userRepository.findAll(pageable).map(this::convertToDto);
    }
    
    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return 用户名存在返回true，否则返回false
     */
    @Override
    public Boolean existsByUsername(String username) {
        // 检查数据库中是否存在指定用户名
        return userRepository.existsByUsername(username);
    }
    
    /**
     * 检查邮箱是否存在
     * @param email 邮箱
     * @return 邮箱存在返回true，否则返回false
     */
    @Override
    public Boolean existsByEmail(String email) {
        // 检查数据库中是否存在指定邮箱
        return userRepository.existsByEmail(email);
    }
    

    /**
     * 将用户实体对象转换为DTO对象
     * @param user 用户实体对象
     * @return 用户DTO对象
     */
    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
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
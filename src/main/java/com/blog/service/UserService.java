package com.blog.service;

import com.blog.dto.UserDto;
import com.blog.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    UserDto createUser(UserDto userDto);
    Optional<UserDto> getUserById(Long id);
    Optional<UserDto> getUserByUsername(String username);
    UserDto updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);
    Page<UserDto> getAllUsers(Pageable pageable);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
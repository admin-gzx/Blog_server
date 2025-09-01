package com.blog.service.impl;

import com.blog.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 用户详情实现类
 * 实现Spring Security的UserDetails接口，用于用户认证和授权
 */
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long id;

    /** 用户名 */
    private String username;

    /** 用户邮箱 */
    private String email;

    /** 用户密码，使用@JsonIgnore注解避免序列化 */
    @JsonIgnore
    private String password;

    /**
     * 构造函数
     * @param id 用户ID
     * @param username 用户名
     * @param email 用户邮箱
     * @param password 用户密码
     */
    public UserDetailsImpl(Long id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * 从User实体对象构建UserDetailsImpl对象的静态方法
     * @param user User实体对象
     * @return UserDetailsImpl对象
     */
    public static UserDetailsImpl build(User user) {
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword());
    }

    /**
     * 获取用户权限集合
     * @return 权限集合
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 为了简化，我们没有实现角色/权限的完整映射
        // 在实际应用中，您需要将用户角色映射到GrantedAuthority
        return List.of(new SimpleGrantedAuthority("USER"));
    }

    /**
     * 获取用户密码
     * @return 用户密码
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * 获取用户名
     * @return 用户名
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * 检查账户是否未过期
     * @return 始终返回true，表示账户未过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 检查账户是否未锁定
     * @return 始终返回true，表示账户未锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 检查凭证是否未过期
     * @return 始终返回true，表示凭证未过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 检查账户是否启用
     * @return 始终返回true，表示账户已启用
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * 重写equals方法，用于比较UserDetailsImpl对象
     * @param o 要比较的对象
     * @return 如果对象相等返回true，否则返回false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
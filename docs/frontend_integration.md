# 前端调用后端API文档

## 1. 概述

本文档详细说明了前端如何调用博客系统的后端API接口。后端使用JWT (JSON Web Token) 进行用户认证，前端需要在请求头中包含有效的JWT令牌才能访问受保护的接口。

## 2. 认证流程

### 2.1 用户登录

用户首先需要通过登录接口获取JWT令牌：

**请求URL**: `POST /api/auth/signin`

**请求示例**:
```javascript
const loginData = {
  username: 'user123',
  password: 'password123'
};

fetch('http://localhost:8080/api/auth/signin', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify(loginData)
})
.then(response => response.json())
.then(data => {
  // 保存JWT令牌到localStorage或sessionStorage
  localStorage.setItem('token', data.token);
  console.log('登录成功，令牌已保存');
})
.catch(error => {
  console.error('登录失败:', error);
});
```

**响应示例**:
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.xxxx",
  "type": "Bearer",
  "id": 1,
  "username": "user123",
  "email": "user123@example.com"
}
```

### 2.2 用户注册

用户可以通过注册接口创建新账户：

**请求URL**: `POST /api/auth/signup`

**请求示例**:
```javascript
const signupData = {
  username: 'newuser',
  password: 'newpassword',
  email: 'newuser@example.com',
  nickname: '新用户'
};

fetch('http://localhost:8080/api/auth/signup', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify(signupData)
})
.then(response => response.json())
.then(data => {
  console.log('注册成功:', data);
})
.catch(error => {
  console.error('注册失败:', error);
});
```

**响应示例**:
```json
{
  "message": "用户注册成功"
}
```

### 2.3 使用JWT令牌

登录成功后，前端需要在后续请求的Authorization头中包含JWT令牌：

```javascript
const token = localStorage.getItem('token');

fetch('http://localhost:8080/api/articles', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`
  },
  body: JSON.stringify(articleData)
})
.then(response => response.json())
.then(data => {
  console.log('文章创建成功:', data);
})
.catch(error => {
  console.error('文章创建失败:', error);
});
```

## 3. 公开接口

以下接口不需要JWT令牌即可访问：

### 3.1 文章相关接口

- `GET /api/articles` - 分页获取所有文章
- `GET /api/articles/{id}` - 根据ID获取文章详情
- `GET /api/articles/published` - 分页获取已发布的文章
- `GET /api/articles/author/{authorId}` - 根据作者ID分页获取文章
- `GET /api/articles/category/{categoryId}` - 根据分类ID分页获取文章
- `GET /api/articles/tag/{tagId}` - 根据标签ID分页获取文章
- `GET /api/articles/popular` - 分页获取热门文章
- `GET /api/articles/popular/top` - 获取热门文章Top5

### 3.2 分类相关接口

- `GET /api/categories` - 分页获取分类列表
- `GET /api/categories/{id}` - 根据ID获取分类
- `GET /api/categories/exists/{name}` - 检查分类名称是否存在

### 3.3 标签相关接口

- `GET /api/tags` - 分页获取标签列表
- `GET /api/tags/{id}` - 根据ID获取标签
- `GET /api/tags/exists/{name}` - 检查标签名称是否存在

### 3.4 用户相关接口

- `GET /api/users/{id}` - 根据ID获取用户
- `GET /api/users` - 分页获取用户列表
- `GET /api/users/exists/username/{username}` - 检查用户名是否存在
- `GET /api/users/exists/email/{email}` - 检查邮箱是否存在

### 3.5 测试接口

- `GET /api/test/all` - 公共测试接口
- `GET /api/test/user` - 用户测试接口
- `GET /api/test/admin` - 管理员测试接口

## 4. 需要认证的接口

以下接口需要有效的JWT令牌才能访问：

### 4.1 文章管理接口

- `POST /api/articles` - 创建文章
- `PUT /api/articles/{id}` - 更新文章
- `DELETE /api/articles/{id}` - 删除文章

### 4.2 用户管理接口

- `POST /api/users` - 创建用户
- `GET /api/users/{id}` - 根据ID获取用户
- `PUT /api/users/{id}` - 更新用户
- `DELETE /api/users/{id}` - 删除用户
- `GET /api/users` - 分页获取用户列表
- `GET /api/users/exists/username/{username}` - 检查用户名是否存在
- `GET /api/users/exists/email/{email}` - 检查邮箱是否存在

### 4.3 分类管理接口

- `POST /api/categories` - 创建分类
- `PUT /api/categories/{id}` - 更新分类
- `DELETE /api/categories/{id}` - 删除分类

### 4.4 标签管理接口

- `POST /api/tags` - 创建标签
- `PUT /api/tags/{id}` - 更新标签
- `DELETE /api/tags/{id}` - 删除标签

## 5. 错误处理

### 5.1 认证错误

如果请求需要认证但未提供有效的JWT令牌，后端将返回401 Unauthorized状态码：

```json
{
  "message": "用户未认证"
}
```

### 5.2 其他常见错误

- 400 Bad Request: 请求参数错误
- 404 Not Found: 请求的资源不存在
- 500 Internal Server Error: 服务器内部错误

## 6. 注意事项

1. JWT令牌默认有效期为24小时（86400秒），过期后需要重新登录
2. 在生产环境中，应使用HTTPS协议以确保令牌传输安全
3. 前端应妥善保管JWT令牌，避免泄露
4. 对于公开接口，不要在请求头中添加Authorization字段，以避免出现JWT令牌格式错误
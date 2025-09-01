# 博客系统API接口文档

## 1. 认证接口

### 1.1 用户登录

| 接口描述 | 请求方式 | 请求路径 | 请求参数 | 响应内容 |
|---------|---------|---------|---------|---------|
| 用户登录 | POST | /api/auth/signin | LoginRequest对象 | JwtResponse对象 |

#### 接口详情

**用户登录**
- 请求URL: `POST /api/auth/signin`
- 请求示例:
```json
{
  "username": "user123",
  "password": "password123"
}
```
- 响应示例:
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.xxxx",
  "type": "Bearer",
  "id": 1,
  "username": "user123",
  "email": "user123@example.com"
}
```

### 1.2 用户注册

| 接口描述 | 请求方式 | 请求路径 | 请求参数 | 响应内容 |
|---------|---------|---------|---------|---------|
| 用户注册 | POST | /api/auth/signup | UserDto对象 | 操作结果 |

#### 接口详情

**用户注册**
- 请求URL: `POST /api/auth/signup`
- 请求示例:
```json
{
  "username": "newuser",
  "password": "newpassword",
  "email": "newuser@example.com",
  "nickname": "新用户"
}
```
- 响应示例:
```json
{
  "message": "用户注册成功"
}
```

## 2. 文章管理API

| 接口描述 | 请求方式 | 请求路径 | 请求参数 | 响应内容 |
|---------|---------|---------|---------|---------|
| 创建文章 | POST | /api/articles | ArticleDto对象 | ArticleDto对象 |
| 更新文章 | PUT | /api/articles/{id} | 文章ID, ArticleDto对象 | ArticleDto对象 |
| 删除文章 | DELETE | /api/articles/{id} | 文章ID | 操作结果 |
| 分页获取所有文章 | GET | /api/articles | 分页参数 | 文章列表 |
| 分页获取已发布的文章 | GET | /api/articles/published | 分页参数 | 文章列表 |
| 根据作者ID分页获取文章 | GET | /api/articles/author/{authorId} | 作者ID, 分页参数 | 文章列表 |
| 根据分类ID分页获取文章 | GET | /api/articles/category/{categoryId} | 分类ID, 分页参数 | 文章列表 |
| 根据标签ID分页获取文章 | GET | /api/articles/tag/{tagId} | 标签ID, 分页参数 | 文章列表 |
| 分页获取热门文章 | GET | /api/articles/popular | 分页参数 | 文章列表 |
| 获取热门文章Top5 | GET | /api/articles/popular/top | 无 | 文章列表 |

### 文章API详情

**创建文章**
- 请求URL: `POST /api/articles`
- 请求示例:
```json
{
  "title": "Spring Boot入门教程",
  "content": "Spring Boot是一个开源的Java框架...",
  "summary": "Spring Boot入门教程摘要",
  "coverImage": "https://example.com/image.jpg",
  "published": true
}
```
- 响应示例:
```json
{
  "id": 1,
  "title": "Spring Boot入门教程",
  "content": "Spring Boot是一个开源的Java框架...",
  "summary": "Spring Boot入门教程摘要",
  "coverImage": "https://example.com/image.jpg",
  "published": true,
  "viewCount": 0,
  "likeCount": 0,
  "createTime": "2023-05-01T12:00:00",
  "updateTime": "2023-05-01T12:00:00",
  "author": {
    "id": 1,
    "username": "author1",
    "email": "author1@example.com",
    "nickname": "作者1",
    "avatar": null,
    "enabled": true,
    "createTime": "2023-05-01T12:00:00",
    "updateTime": "2023-05-01T12:00:00"
  }
}
```

**更新文章**
- 请求URL: `PUT /api/articles/{id}`
- 请求示例:
```json
{
  "title": "Spring Boot进阶教程",
  "content": "Spring Boot是一个开源的Java框架...",
  "summary": "Spring Boot进阶教程摘要",
  "coverImage": "https://example.com/image.jpg",
  "published": true
}
```
- 响应示例:
```json
{
  "id": 1,
  "title": "Spring Boot进阶教程",
  "content": "Spring Boot是一个开源的Java框架...",
  "summary": "Spring Boot进阶教程摘要",
  "coverImage": "https://example.com/image.jpg",
  "published": true,
  "viewCount": 10,
  "likeCount": 5,
  "createTime": "2023-05-01T12:00:00",
  "updateTime": "2023-05-02T12:00:00",
  "author": {
    "id": 1,
    "username": "author1",
    "email": "author1@example.com",
    "nickname": "作者1",
    "avatar": null,
    "enabled": true,
    "createTime": "2023-05-01T12:00:00",
    "updateTime": "2023-05-01T12:00:00"
  }
}
```

**删除文章**
- 请求URL: `DELETE /api/articles/{id}`
- 响应示例:
```json
{
  "success": true,
  "data": null
}
```

**分页获取所有文章**
- 请求URL: `GET /api/articles?page=0&size=10`
- 响应示例:
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "title": "Spring Boot入门教程",
      "content": "Spring Boot是一个开源的Java框架...",
      "summary": "Spring Boot入门教程摘要",
      "coverImage": "https://example.com/image.jpg",
      "published": true,
      "viewCount": 0,
      "likeCount": 0,
      "createTime": "2023-05-01T12:00:00",
      "updateTime": "2023-05-01T12:00:00",
      "author": {
        "id": 1,
        "username": "author1",
        "email": "author1@example.com",
        "nickname": "作者1",
        "avatar": null,
        "enabled": true,
        "createTime": "2023-05-01T12:00:00",
        "updateTime": "2023-05-01T12:00:00"
      }
    }
  ]
}
```

## 3. 分类管理API

| 接口描述 | 请求方式 | 请求路径 | 请求参数 | 响应内容 |
|---------|---------|---------|---------|---------|
| 创建分类 | POST | /api/categories | CategoryDto对象 | CategoryDto对象 |
| 根据ID获取分类 | GET | /api/categories/{id} | 分类ID | CategoryDto对象 |
| 更新分类 | PUT | /api/categories/{id} | 分类ID, CategoryDto对象 | CategoryDto对象 |
| 删除分类 | DELETE | /api/categories/{id} | 分类ID | 操作结果 |
| 分页获取分类列表 | GET | /api/categories | 分页参数 | 分类列表 |
| 检查分类名称是否存在 | GET | /api/categories/exists/{name} | 分类名称 | Boolean |

### 分类API详情

**创建分类**
- 请求URL: `POST /api/categories`
- 请求示例:
```json
{
  "name": "技术",
  "description": "技术类文章"
}
```
- 响应示例:
```json
{
  "id": 1,
  "name": "技术",
  "description": "技术类文章",
  "createTime": "2023-05-01T12:00:00",
  "updateTime": "2023-05-01T12:00:00"
}
```

**根据ID获取分类**
- 请求URL: `GET /api/categories/{id}`
- 响应示例:
```json
{
  "id": 1,
  "name": "技术",
  "description": "技术类文章",
  "createTime": "2023-05-01T12:00:00",
  "updateTime": "2023-05-01T12:00:00"
}
```

**更新分类**
- 请求URL: `PUT /api/categories/{id}`
- 请求示例:
```json
{
  "name": "编程",
  "description": "编程类文章"
}
```
- 响应示例:
```json
{
  "id": 1,
  "name": "编程",
  "description": "编程类文章",
  "createTime": "2023-05-01T12:00:00",
  "updateTime": "2023-05-02T12:00:00"
}
```

**删除分类**
- 请求URL: `DELETE /api/categories/{id}`
- 响应示例:
```json
{
  "success": true,
  "data": null
}
```

**分页获取分类列表**
- 请求URL: `GET /api/categories?page=0&size=10`
- 响应示例:
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "name": "技术",
      "description": "技术类文章",
      "createTime": "2023-05-01T12:00:00",
      "updateTime": "2023-05-01T12:00:00"
    }
  ]
}
```

**检查分类名称是否存在**
- 请求URL: `GET /api/categories/exists/{name}`
- 响应示例:
```json
{
  "exists": true
}
```

## 4. 标签管理API

| 接口描述 | 请求方式 | 请求路径 | 请求参数 | 响应内容 |
|---------|---------|---------|---------|---------|
| 创建标签 | POST | /api/tags | TagDto对象 | TagDto对象 |
| 根据ID获取标签 | GET | /api/tags/{id} | 标签ID | TagDto对象 |
| 更新标签 | PUT | /api/tags/{id} | 标签ID, TagDto对象 | TagDto对象 |
| 删除标签 | DELETE | /api/tags/{id} | 标签ID | 操作结果 |
| 分页获取标签列表 | GET | /api/tags | 分页参数 | 标签列表 |
| 检查标签名称是否存在 | GET | /api/tags/exists/{name} | 标签名称 | Boolean |

### 标签API详情

**创建标签**
- 请求URL: `POST /api/tags`
- 请求示例:
```json
{
  "name": "Spring Boot",
  "description": "Spring Boot相关文章"
}
```
- 响应示例:
```json
{
  "id": 1,
  "name": "Spring Boot",
  "description": "Spring Boot相关文章",
  "createTime": "2023-05-01T12:00:00",
  "updateTime": "2023-05-01T12:00:00"
}
```

**根据ID获取标签**
- 请求URL: `GET /api/tags/{id}`
- 响应示例:
```json
{
  "id": 1,
  "name": "Spring Boot",
  "description": "Spring Boot相关文章",
  "createTime": "2023-05-01T12:00:00",
  "updateTime": "2023-05-01T12:00:00"
}
```

**更新标签**
- 请求URL: `PUT /api/tags/{id}`
- 请求示例:
```json
{
  "name": "Spring Framework",
  "description": "Spring Framework相关文章"
}
```
- 响应示例:
```json
{
  "id": 1,
  "name": "Spring Framework",
  "description": "Spring Framework相关文章",
  "createTime": "2023-05-01T12:00:00",
  "updateTime": "2023-05-02T12:00:00"
}
```

**删除标签**
- 请求URL: `DELETE /api/tags/{id}`
- 响应示例:
```json
{
  "success": true,
  "data": null
}
```

**分页获取标签列表**
- 请求URL: `GET /api/tags?page=0&size=10`
- 响应示例:
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "name": "Spring Boot",
      "description": "Spring Boot相关文章",
      "createTime": "2023-05-01T12:00:00",
      "updateTime": "2023-05-01T12:00:00"
    }
  ]
}
```

**检查标签名称是否存在**
- 请求URL: `GET /api/tags/exists/{name}`
- 响应示例:
```json
{
  "exists": true
}
```

## 5. 用户管理API

| 接口描述 | 请求方式 | 请求路径 | 请求参数 | 响应内容 |
|---------|---------|---------|---------|---------|
| 创建用户 | POST | /api/users | UserDto对象 | UserDto对象 |
| 根据ID获取用户 | GET | /api/users/{id} | 用户ID | UserDto对象 |
| 分页获取用户列表 | GET | /api/users | 分页参数 | 用户列表 |
| 检查用户名是否存在 | GET | /api/users/exists/username/{username} | 用户名 | Boolean |

### 用户API详情

**创建用户**
- 请求URL: `POST /api/users`
- 请求示例:
```json
{
  "username": "newuser",
  "password": "newpassword",
  "email": "newuser@example.com",
  "nickname": "新用户"
}
```
- 响应示例:
```json
{
  "success": true,
  "data": {
    "id": 1,
    "username": "newuser",
    "password": null,
    "email": "newuser@example.com",
    "nickname": "新用户",
    "avatar": null,
    "enabled": true,
    "createTime": "2023-05-01T12:00:00",
    "updateTime": "2023-05-01T12:00:00"
  }
}
```

**根据ID获取用户**
- 请求URL: `GET /api/users/{id}`
- 响应示例:
```json
{
  "success": true,
  "data": {
    "id": 1,
    "username": "user1",
    "password": null,
    "email": "user1@example.com",
    "nickname": "用户1",
    "avatar": null,
    "enabled": true,
    "createTime": "2023-05-01T12:00:00",
    "updateTime": "2023-05-01T12:00:00"
  }
}
```

**分页获取用户列表**
- 请求URL: `GET /api/users?page=0&size=10`
- 响应示例:
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "username": "user1",
      "password": null,
      "email": "user1@example.com",
      "nickname": "用户1",
      "avatar": null,
      "enabled": true,
      "createTime": "2023-05-01T12:00:00",
      "updateTime": "2023-05-01T12:00:00"
    }
  ]
}
```

**检查用户名是否存在**
- 请求URL: `GET /api/users/exists/username/{username}`
- 响应示例:
```json
{
  "success": true,
  "data": true
}
```

## 6. 测试接口

| 接口描述 | 请求方式 | 请求路径 | 请求参数 | 响应内容 |
|---------|---------|---------|---------|---------|
| 公共测试接口 | GET | /api/test/all | 无 | 消息 |
| 用户测试接口 | GET | /api/test/user | 无 | 消息 |
| 管理员测试接口 | GET | /api/test/admin | 无 | 消息 |

### 测试API详情

**公共测试接口**
- 请求URL: `GET /api/test/all`
- 响应示例:
```json
{
  "message": "欢迎访问博客系统公共测试接口"
}
```

**用户测试接口**
- 请求URL: `GET /api/test/user`
- 响应示例:
```json
{
  "message": "欢迎访问博客系统用户测试接口"
}
```

**管理员测试接口**
- 请求URL: `GET /api/test/admin`
- 响应示例:
```json
{
  "message": "欢迎访问博客系统管理员测试接口"
}
```
# 企业级博客系统后端项目

## 项目概述
这是一个使用Java开发的企业级博客系统后端项目，采用Spring Boot框架构建，实现了用户管理、文章管理等核心功能模块。

## JWT认证
项目使用JWT (JSON Web Token) 进行用户认证。如果遇到认证相关问题，请检查以下几点：
1. 确保使用足够长的密钥（至少32个字符）
2. 确保用户具有正确的权限集合
3. 确保数据库中有有效的用户数据

常见的认证错误及解决方案：
- `Full authentication is required to access this resource`: 检查请求是否包含有效的JWT令牌，或检查用户权限配置
- 如果访问`/api/articles`接口返回401错误，请检查JwtAuthTokenFilter和WebSecurityConfig中的路径配置是否正确，确保`/api/articles`被正确配置为公开路径

## 技术栈
- Java 11+
- Spring Boot 3.1.0
- Spring Security
- Spring Data JPA
- MySQL 8.0
- Maven 3.8.x
- JWT (JSON Web Token)
- Lombok
- Swagger UI

## 项目结构
```
Blog_server/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── blog/
│   │   │           ├── BlogApplication.java
│   │   │           ├── config/
│   │   │           ├── controller/
│   │   │           ├── dto/
│   │   │           ├── entity/
│   │   │           ├── exception/
│   │   │           ├── repository/
│   │   │           ├── service/
│   │   │           │   └── impl/
│   │   │           └── util/
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── data.sql
│   │       └── schema.sql
│   └── test/
│       └── java/
│           └── com/
│               └── blog/
└── pom.xml
```

## 功能模块
1. 用户管理模块
   - 用户注册与登录
   - JWT Token认证
   - 用户信息管理
2. 文章管理模块
   - 文章的增删改查
   - 文章分类和标签
   - 文章浏览量统计
3. 分类管理模块
   - 分类的增删改查
4. 标签管理模块
   - 标签的增删改查
   - 文章与标签的多对多关联

## 接口文档
项目集成了Swagger UI，启动后可通过`http://localhost:8080/swagger-ui/index.html`访问接口文档。

## 快速开始
1. 克隆项目
2. 配置数据库连接
3. 运行`mvn spring-boot:run`启动项目

## API接口文档
有关所有API接口的详细说明，请参考[API接口文档](docs/api.md)。
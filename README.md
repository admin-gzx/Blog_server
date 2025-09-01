# 企业级博客系统后端项目

## 项目概述
这是一个使用Java开发的企业级博客系统后端项目，采用Spring Boot框架构建，实现了用户管理、文章管理等核心功能模块。

## 技术栈
- Java 11+
- Spring Boot 2.7.x
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
│   │   │           ├── entity/
│   │   │           ├── repository/
│   │   │           ├── service/
│   │   │           │   └── impl/
│   │   │           └── dto/
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── static/
│   │       └── templates/
│   └── test/
│       └── java/
│           └── com/
│               └── blog/
└── pom.xml
```

## 功能模块
1. 用户管理模块
2. 文章管理模块
3. 分类管理模块
4. 标签管理模块

## 接口文档
项目集成了Swagger UI，启动后可通过`http://localhost:8080/swagger-ui.html`访问接口文档。

## 快速开始
1. 克隆项目
2. 配置数据库连接
3. 运行`mvn spring-boot:run`启动项目

## 项目文档
详细的设计文档和接口说明请参考[项目文档](docs/PROJECT.md)。
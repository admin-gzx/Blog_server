package com.blog.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger配置类
 * 配置OpenAPI文档的基本信息，包括标题、描述、版本、联系人和许可证等。
 */
@Configuration
public class SwaggerConfig {

    /**
     * 创建自定义OpenAPI Bean
     * 配置API文档的基本信息，如标题、描述、版本、联系人和许可证等。
     * @return OpenAPI实例
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("博客系统API文档")
                        .description("这是一个企业级博客系统的后端API文档")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("开发团队")
                                .url("https://github.com")
                                .email("developer@blog.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}
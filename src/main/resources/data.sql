-- 插入默认用户 (密码为 'admin123' 的BCrypt加密结果)
INSERT INTO users (username, email, password) VALUES 
('admin', 'admin@example.com', '$2a$10$wQ8vIvq/JcsB86K.GiBSNe.W9.Dr/GsW3b4rk8oLWuH1bEWW/D91G'),
('user1', 'user1@example.com', '$2a$10$wQ8vIvq/JcsB86K.GiBSNe.W9.Dr/GsW3b4rk8oLWuH1bEWW/D91G'),
('user2', 'user2@example.com', '$2a$10$wQ8vIvq/JcsB86K.GiBSNe.W9.Dr/GsW3b4rk8oLWuH1bEWW/D91G');

-- 插入默认分类
INSERT INTO categories (name, description) VALUES 
('技术', '技术相关文章'),
('生活', '生活相关文章'),
('旅行', '旅行相关文章');

-- 插入默认标签
INSERT INTO tags (name, description) VALUES 
('Java', 'Java编程语言'),
('Spring', 'Spring框架'),
('MySQL', 'MySQL数据库');

-- 插入默认文章
INSERT INTO articles (title, content, summary, author_id, category_id, is_published, published_at) VALUES 
('Spring Boot入门', 'Spring Boot是一个开源的Java框架...', 'Spring Boot入门教程', 1, 1, 1, NOW()),
('MySQL优化技巧', 'MySQL是世界上最流行的开源数据库之一...', 'MySQL性能优化指南', 1, 1, 1, NOW()),
('我的旅行日记', '今天我去了一个美丽的地方...', '旅行日记分享', 2, 3, 1, NOW());

-- 插入文章标签关联
INSERT INTO article_tags (article_id, tag_id) VALUES 
(1, 1), (1, 2),
(2, 1), (2, 3),
(3, 3);
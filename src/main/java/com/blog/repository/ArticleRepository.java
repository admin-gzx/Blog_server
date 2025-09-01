package com.blog.repository;

import com.blog.entity.Article;
import com.blog.entity.Category;
import com.blog.entity.Tag;
import com.blog.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findByPublishedTrueOrderByCreateTimeDesc(Pageable pageable);
    Page<Article> findByAuthorAndPublishedTrueOrderByCreateTimeDesc(User author, Pageable pageable);
    Page<Article> findByCategoryAndPublishedTrueOrderByCreateTimeDesc(Category category, Pageable pageable);
    Page<Article> findByTagsContainingAndPublishedTrueOrderByCreateTimeDesc(Tag tag, Pageable pageable);
    
    @Query("SELECT a FROM Article a WHERE a.published = true ORDER BY a.viewCount DESC")
    Page<Article> findPopularArticles(Pageable pageable);
    
    List<Article> findTop5ByPublishedTrueOrderByViewCountDesc();
}
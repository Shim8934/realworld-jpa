package com.io.realworldjpa.domain.article.service;

import com.io.realworldjpa.domain.article.entity.Article;
import com.io.realworldjpa.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Optional<Article> findBySlug(String slug);

    boolean existsBySlug(String slug);

    @Query("""
            SELECT article FROM Article article
            WHERE (:tag IS NULL OR :tag IN (SELECT t.tag.value FROM article.tagList t))
            AND (:author IS NULL OR article.author.profile.username = :author)
            AND (:favorited IS NULL OR :favorited IN (SELECT favoruser.user.profile.username FROM article.favoriteUsers favoruser))
            ORDER BY article.createdAt DESC
            """)
    Page<Article> findByFilter(
            @Param("tag") String tag,
            @Param("author") String author,
            @Param("favorited") String favorited,
            Pageable pageable);

    Page<Article> findByAuthorInOrderByCreatedAtDesc(Collection<User> authors, Pageable pageable);
}

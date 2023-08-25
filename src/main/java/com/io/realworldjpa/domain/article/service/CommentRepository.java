package com.io.realworldjpa.domain.article.service;

import com.io.realworldjpa.domain.article.entity.Article;
import com.io.realworldjpa.domain.article.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Set<Comment> findByArticleOrderByCreatedAtDesc(Article article);

    boolean existsById(long id);

}

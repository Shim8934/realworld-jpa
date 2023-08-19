package com.io.realworldjpa.domain.article.entity;

import com.io.realworldjpa.domain.user.entity.User;
import com.io.realworldjpa.global.util.Generated;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "article_favorite")
@EntityListeners(AuditingEntityListener.class)
public class ArticleFavorite {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "favorite_id")
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "article_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    protected ArticleFavorite() {

    }

    public ArticleFavorite(User user, Article article) {
        this.user = user;
        this.article = article;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Article getArticle() {
        return article;
    }

    @Override
    @Generated
    public boolean equals(Object o) {
        return o instanceof ArticleFavorite favorite
                && Objects.equals(this.user, favorite.user)
                && Objects.equals(this.article, favorite.article);
    }

    @Override
    @Generated
    public int hashCode() {
        return Objects.hash(this.id, this.user, this.article);
    }
}

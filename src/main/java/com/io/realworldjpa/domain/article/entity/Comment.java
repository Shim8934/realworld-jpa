package com.io.realworldjpa.domain.article.entity;

import com.io.realworldjpa.domain.user.entity.User;
import com.io.realworldjpa.global.util.Generated;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@Entity
@Table(name = "comments")
@EntityListeners(AuditingEntityListener.class)
public class Comment {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(name = "body", nullable = false)
    private String body;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    private LocalDateTime updatedAt;

    protected Comment() {}

    private Comment(Long id, Article article, User author, String body, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.article = article;
        this.author = author;
        this.body = body;
        this.createdAt = defaultIfNull(createdAt, LocalDateTime.now());
        this.updatedAt = updatedAt;
    }

    private Comment(Long id, Article article, User author, String body) {
        this(null, article, author, body, null, null);
    }

    public Long getId() {
        return id;
    }

    public Article getArticle() {
        return article;
    }

    public User getAuthor() {
        return author;
    }

    public String getBody() {
        return body;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean isNotPostByMe(User writer) {
        return !this.author.equals(writer);
    }

    @Override
    @Generated
    public boolean equals(Object o) {
        return o instanceof Comment comment
                && Objects.equals(this.id, comment.id);
    }

    @Override
    @Generated
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Generated
    static public class Builder {
        private Long id;
        private User author;
        private Article article;
        private String body;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder author(User author) {
            this.author = author;
            return this;
        }

        public Builder article(Article article) {
            this.article = article;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Comment build() {
            return new Comment(id, article, author, body);
        }
    }
}

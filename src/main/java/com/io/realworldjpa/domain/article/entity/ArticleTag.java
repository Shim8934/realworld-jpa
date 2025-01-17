package com.io.realworldjpa.domain.article.entity;

import com.io.realworldjpa.global.util.Generated;
import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "article_tag")
@EntityListeners(AuditingEntityListener.class)
public class ArticleTag {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @JoinColumn(name = "article_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Article article;

    @JoinColumn(name = "tag_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Tag tag;

    protected ArticleTag() {}

    public ArticleTag(Article article, Tag tag) {
        this.article = article;
        this.tag = tag;
    }

    @Generated
    public Long getId() {
        return id;
    }

    @Generated
    public Article getArticle() {
        return article;
    }

    @Generated
    public Tag getTag() {
        return tag;
    }

    @Override
    @Generated
    public boolean equals(Object o) {
        return o instanceof ArticleTag articleTag
                && Objects.equals(this.article, articleTag.article)
                && Objects.equals(this.tag, articleTag.tag);
    }

    @Override
    @Generated
    public int hashCode() {
        return Objects.hash(this.id, this.article, this.tag);
    }
}

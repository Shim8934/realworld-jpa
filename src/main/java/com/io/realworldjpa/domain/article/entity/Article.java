package com.io.realworldjpa.domain.article.entity;

import com.io.realworldjpa.domain.user.entity.User;
import com.io.realworldjpa.global.util.Generated;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static jakarta.persistence.GenerationType.IDENTITY;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Entity
@Table(name = "articles")
@EntityListeners(AuditingEntityListener.class)
public class Article {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "article_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(unique = true, nullable = false)
    private String slug;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(name = "body", nullable = false)
    private String content = "";

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ArticleTag> tagList = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ArticleFavorite> favoriteUsers = new HashSet<>();

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    private LocalDateTime updatedAt;

    protected Article() {}

    private Article(Long id, User author, String slug, String title, String description, String content, Set<ArticleTag> tagList, Set<ArticleFavorite> favoriteUsers, LocalDateTime createdAt) {
        this.id = id;
        this.author = author;
        this.slug = createSlugByTitle(title);
        this.title = title;
        this.description = description;
        this.content = content;
        this.tagList = new HashSet<>();
        this.favoriteUsers = new HashSet<>();
        this.createdAt = defaultIfNull(createdAt, LocalDateTime.now());
    }

    public Article(Long id, User author, String title, String description, String content) {
        this(null, author, null, title, description, content, null, null, null);
    }

    private String createSlugByTitle(String title) {
        checkArgument(isNotBlank(title), "제목은 반드시 제공되야 합니다.");

        return title.toLowerCase().replaceAll("\\s+", "-");
    }

    public void addTag(Tag tag) {
        checkArgument(isNotEmpty(tag), "태그(게시글용)는 반드시 제공되야 합니다.");

        ArticleTag articleTags = new ArticleTag(this, tag);

        if(this.tagList.stream().anyMatch(articleTags::equals)) {
            return ;
        }

        this.tagList.add(articleTags);
    }

    public List<Tag> getTags() {
        return this.tagList.stream().map(ArticleTag::getTag).toList();
    }

    public String[] getTagsValues() {
        return this.getTags().stream().map(Tag::getValue).sorted().toArray(String[]::new);
    }

    @Generated
    public boolean equalsArticle(ArticleFavorite articleFavorite) {
        return articleFavorite.getArticle().equals(this);
    }

    public Long getId() {
        return id;
    }

    public User getAuthor() {
        return author;
    }

    public String getSlug() {
        return slug;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getContent() {
        return content;
    }

    public Set<ArticleFavorite> getFavoriteUsers() {
        return favoriteUsers;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public int numberOfFavorites() {
        return this.favoriteUsers.size();
    }

    public boolean isNotWrittenByMe(User writer) {
        return !this.author.equals(writer);
    }

    public void updateTitle(String title) {
        this.title = title;
        this.slug = createSlugByTitle(title);
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    @Override
    @Generated
    public boolean equals(Object o) {
        return o instanceof Article article
                && Objects.equals(this.id, article.id);
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
        private String title;
        private String description;
        private String content;

        public Builder() {}

        public Builder(Article article) {
            this.id = article.id;
            this.author = article.author;
            this.title = article.title;
            this.description = article.description;
            this.content = article.content;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder author(User author) {
            this.author = author;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Article build() {
            return new Article(id, author, title, description, content);
        }
    }
}

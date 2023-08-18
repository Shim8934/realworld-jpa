package com.io.realworldjpa.domain.article.entity;

import com.io.realworldjpa.global.util.Generated;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static jakarta.persistence.GenerationType.IDENTITY;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

@Entity
@Table(name = "tags")
@EntityListeners(AuditingEntityListener.class)
public class Tag {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @Column(name = "tags_value", unique = true, nullable = false)
    private String value;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    protected Tag() {}

    public Tag(String value) {
        this.value = value;
    }

    public void addTagsToArticle(Article article) {
        checkArgument(isNotEmpty(article), "게시글(태그용)은 반드시 제공되야 합니다.");

        article.addTag(this);
    }

    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    @Generated
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("value", value)
                .toString();
    }

    @Override
    @Generated
    public boolean equals(Object o) {
        return o instanceof Tag tag
                && Objects.equals(this.value, tag.value);
    }

    @Override
    @Generated
    public int hashCode() {
        return Objects.hash(this.value);
    }
}

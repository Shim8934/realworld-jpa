package com.io.realworldjpa.domain.article.entity;

import com.io.realworldjpa.domain.user.entity.ProfileDto;
import com.io.realworldjpa.domain.user.entity.User;

import java.time.LocalDateTime;

public record ArticleDto(
        String slug, String title, String description,
        String body, String[] tagList,
        LocalDateTime createdAt, LocalDateTime updatedAt,
        boolean favorited, int favoritesCount,
        ProfileDto author
) {

    public ArticleDto(User user, Article article) {
        this(
                article.getSlug(), article.getTitle(), article.getDescription(),
                article.getBody(), article.getTagsValues(),
                article.getCreatedAt(), article.getUpdatedAt(),
                user.isAlreadyFavorite(article), article.numberOfFavorites(),
                new ProfileDto(user, article.getAuthor())
        );
    }
}

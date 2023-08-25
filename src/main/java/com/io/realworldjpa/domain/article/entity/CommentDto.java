package com.io.realworldjpa.domain.article.entity;

import com.io.realworldjpa.domain.user.entity.ProfileDto;
import com.io.realworldjpa.domain.user.entity.User;

import java.time.LocalDateTime;

import static com.io.realworldjpa.domain.user.entity.ProfileDto.anonymousProfile;
import static com.io.realworldjpa.domain.user.entity.ProfileDto.followProfile;

public record CommentDto(Long id, String body, LocalDateTime createdAt, LocalDateTime updatedAt, ProfileDto author) {

    public CommentDto(User user, Comment comment) {
        this(comment.getId(), comment.getBody(), comment.getCreatedAt(), comment.getUpdatedAt(),
                new ProfileDto(user, comment.getAuthor()));
    }

    public static CommentDto followProfileComment(Comment comment) {
        return new CommentDto(comment.getId(), comment.getBody(), comment.getCreatedAt(), comment.getUpdatedAt(),
                followProfile(comment.getAuthor()));
    }

    public static CommentDto unfollowProfileComment(Comment comment) {
        return new CommentDto(comment.getId(), comment.getBody(), comment.getCreatedAt(), comment.getUpdatedAt(),
                anonymousProfile(comment.getAuthor()));
    }
}

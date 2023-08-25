package com.io.realworldjpa.domain.article.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.io.realworldjpa.domain.article.entity.CommentDto;

import java.util.List;

public record MultipleCommentRecord(@JsonUnwrapped @JsonProperty("comments") CommentDto[] comments) {
    public MultipleCommentRecord(List<CommentDto> commentDtos) {
        this(commentDtos.toArray(CommentDto[]::new));
    }
}

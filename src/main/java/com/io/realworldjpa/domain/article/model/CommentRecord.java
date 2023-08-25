package com.io.realworldjpa.domain.article.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.io.realworldjpa.domain.article.entity.CommentDto;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeName("comment")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = NAME)
public record CommentRecord(@JsonValue CommentDto commentDto) {
}

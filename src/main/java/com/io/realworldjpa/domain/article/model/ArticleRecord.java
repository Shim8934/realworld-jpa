package com.io.realworldjpa.domain.article.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.io.realworldjpa.domain.article.entity.ArticleDto;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeName("article")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = NAME)
public record ArticleRecord(@JsonValue ArticleDto articleDto) {
}

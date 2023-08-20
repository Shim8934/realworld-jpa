package com.io.realworldjpa.domain.article.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.io.realworldjpa.domain.article.entity.ArticleDto;

import java.util.List;

public record MultipleArticleRecord(
        @JsonUnwrapped
        @JsonProperty("articles") List<ArticleDto> articles,
        @JsonProperty("articlesCount") int articlesCount) {

    public MultipleArticleRecord(List<ArticleDto> articleList) {
        this(articleList, articleList.size());
    }
}

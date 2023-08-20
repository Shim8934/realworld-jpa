package com.io.realworldjpa.domain.article.model;

import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.List;

@JsonRootName("article")
public record ArticlePostRequest(String title, String description, String body, List<String> tagList) {}

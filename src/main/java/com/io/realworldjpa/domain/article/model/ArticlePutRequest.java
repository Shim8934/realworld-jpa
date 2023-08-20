package com.io.realworldjpa.domain.article.model;

import com.fasterxml.jackson.annotation.JsonRootName;


@JsonRootName("article")
public record ArticlePutRequest(String title, String description, String body) {}

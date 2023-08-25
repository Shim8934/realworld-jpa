package com.io.realworldjpa.domain.article.model;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("comment")
public record CommentPostRequest(String body) {}

package com.io.realworldjpa.domain.article.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.List;

public record TagsRecord(@JsonUnwrapped @JsonProperty("tags") String[] tags) {

    public TagsRecord(List<String> tags) {
        this(tags.toArray(String[]::new));
    }
}

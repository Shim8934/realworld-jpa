package com.io.realworldjpa.domain.article.model;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public record MultipleArticleRequest(String tag, String author, String favorited, Integer limit, Integer offset) {

    public MultipleArticleRequest {
        if (offset == null || offset < 0) {
            offset = 0;
        }

        if (limit == null || limit < 0 || limit > 50) {
            limit = 20;
        }
    }

    public Pageable getPageable() {
        return PageRequest.of(offset, limit);
    }
}

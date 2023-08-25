package com.io.realworldjpa.domain.article.controller;

import com.io.realworldjpa.domain.article.model.TagsRecord;
import com.io.realworldjpa.domain.article.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/api/tags")
    public TagsRecord getTags() {
        List<String> tags = tagService.getTags();
        return new TagsRecord(tags);
    }

}

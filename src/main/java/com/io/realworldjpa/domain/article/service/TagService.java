package com.io.realworldjpa.domain.article.service;

import com.io.realworldjpa.domain.article.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    @Transactional(readOnly = true)
    public List<String> getTags() {
        return tagRepository.findAll().stream().map(Tag::getValue).toList();
    }

}

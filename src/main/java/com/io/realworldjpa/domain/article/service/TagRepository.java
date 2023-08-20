package com.io.realworldjpa.domain.article.service;

import com.io.realworldjpa.domain.article.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByValue(String value);

}

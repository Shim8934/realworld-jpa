package com.io.realworldjpa.domain.article.service;

import com.io.realworldjpa.domain.article.entity.Article;
import com.io.realworldjpa.domain.article.entity.ArticleDto;
import com.io.realworldjpa.domain.article.entity.Tag;
import com.io.realworldjpa.domain.article.model.ArticlePostRequest;
import com.io.realworldjpa.domain.article.model.ArticlePutRequest;
import com.io.realworldjpa.domain.article.model.MultipleArticleRequest;
import com.io.realworldjpa.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final TagRepository tagRepository;

    @Transactional(readOnly = true)
    public ArticleDto getArticle(User reader, String slug) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new NoSuchElementException("게시글 ['%s'] 이 존재하지 않습니다.".formatted(slug)));
        return new ArticleDto(reader, article);
    }

    @Transactional(readOnly = true)
    public List<ArticleDto> getArticles(User reader, MultipleArticleRequest multipleArticleRequest) {
        String tag = multipleArticleRequest.tag();
        String author = multipleArticleRequest.author();
        String favorited = multipleArticleRequest.favorited();

        Page<Article> articles = articleRepository.findByFilter(tag, author, favorited, multipleArticleRequest.getPageable());

        return articles.getContent()
                .stream()
                .map(article -> new ArticleDto(reader, article))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ArticleDto> getFeedArticles(User reader, MultipleArticleRequest multipleArticleRequest) {
        List<User> readersFollowings = reader.followUserList();

        Page<Article> articles = articleRepository.findByAuthorInOrderByCreatedAtDesc(readersFollowings, multipleArticleRequest.getPageable());

        return articles.getContent()
                .stream()
                .map(article -> new ArticleDto(reader, article))
                .toList();
    }

    @Transactional
    public ArticleDto createArticle(User author, ArticlePostRequest articlePostRequest) {
        Article article = new Article.Builder()
                .title(articlePostRequest.title())
                .description(articlePostRequest.description())
                .content(articlePostRequest.body())
                .author(author)
                .build();

        for (String tagValue : articlePostRequest.tagList()) {
            Optional<Tag> checkTag = tagRepository.findByValue(tagValue);
            Tag tag = checkTag.orElseGet(() -> tagRepository.save(new Tag(tagValue)));
            tag.addTagsToArticle(article);
        }

        article = articleRepository.save(article);
        return new ArticleDto(author, article);
    }

    @Transactional
    public ArticleDto updateArticle(User editor, String slug, ArticlePutRequest articlePutRequest) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new NoSuchElementException("게시글 ['%s'] 이 존재하지 않습니다.".formatted(slug)));

        if (article.isNotWrittenByMe(editor)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        article.updateTitle(articlePutRequest.title());
        article.updateDescription(articlePutRequest.description());
        article.updateContent(articlePutRequest.body());

        return new ArticleDto(editor, article);
    }

    @Transactional
    public void deleteArticle(User author, String slug) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new NoSuchElementException("게시글 ['%s'] 이 존재하지 않습니다.".formatted(slug)));

        if (article.isNotWrittenByMe(author)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        articleRepository.delete(article);
    }
}

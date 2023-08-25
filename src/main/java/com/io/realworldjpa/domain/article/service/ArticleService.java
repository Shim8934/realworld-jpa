package com.io.realworldjpa.domain.article.service;

import com.io.realworldjpa.domain.article.entity.*;
import com.io.realworldjpa.domain.article.model.ArticlePostRequest;
import com.io.realworldjpa.domain.article.model.ArticlePutRequest;
import com.io.realworldjpa.domain.article.model.CommentPostRequest;
import com.io.realworldjpa.domain.article.model.MultipleArticleRequest;
import com.io.realworldjpa.domain.user.entity.User;
import com.io.realworldjpa.domain.user.service.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static com.io.realworldjpa.domain.article.entity.CommentDto.*;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;
    private final FollowRepository followRepository;

    @Transactional(readOnly = true)
    public ArticleDto getArticle(User reader, String slug) {
        Article article = findArticleBySlug(slug);
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
                .body(articlePostRequest.body())
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
        Article article = findArticleBySlug(slug);

        if (article.isNotPostByMe(editor)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        article.updateTitle(articlePutRequest.title());
        article.updateDescription(articlePutRequest.description());
        article.updateBody(articlePutRequest.body());

        return new ArticleDto(editor, article);
    }

    @Transactional
    public void deleteArticle(User author, String slug) {
        Article article = findArticleBySlug(slug);

        if (article.isNotPostByMe(author)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        articleRepository.delete(article);
    }

    @Transactional
    public CommentDto createComment(User author, String slug, CommentPostRequest commentPostRequest) {
        Article article = findArticleBySlug(slug);

        Comment comment = new Comment.Builder()
                .article(article)
                .author(author)
                .body(commentPostRequest.body())
                .build();

        commentRepository.save(comment);

        return new CommentDto(author, comment);
    }

    public List<CommentDto> getArticleComments(User reader, String slug) {
        Article article = findArticleBySlug(slug);

        Set<Comment> comments = commentRepository.findByArticleOrderByCreatedAtDesc(article);

        if (reader.isAnonymous()) {
            return comments.stream().map(CommentDto::unfollowProfileComment).toList();
        }

        return comments.stream()
                .map(comment -> {
                    User commentAuthor = comment.getAuthor();
                    return followRepository.existsByFromAndTo(reader, commentAuthor)
                            ? followProfileComment(comment)
                            : unfollowProfileComment(comment);
                }).toList();
    }

    @Transactional
    public void deleteComment(User author, long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("댓글 ['id: %d'] 이 존재하지 않습니다.".formatted(commentId)));

        if (comment.isNotPostByMe(author)) {
            throw new IllegalArgumentException("댓글 삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }

    @Transactional
    public ArticleDto favoriteArticle(User reader, String slug) {
        Article article = findArticleBySlug(slug);

        reader.favoriteArticle(article);

        return new ArticleDto(reader, article);
    }

    @Transactional
    public ArticleDto unFavoriteArticle(User reader, String slug) {
        Article article = findArticleBySlug(slug);

        reader.unfavoriteArticle(article);

        return new ArticleDto(reader, article);
    }

    private Article findArticleBySlug(String slug) {
        return articleRepository.findBySlug(slug)
                .orElseThrow(() -> new NoSuchElementException("게시글 ['%s'] 이 존재하지 않습니다.".formatted(slug)));
    }
}

package com.io.realworldjpa.domain.article.controller;


import com.io.realworldjpa.domain.article.model.*;
import com.io.realworldjpa.domain.article.service.ArticleService;
import com.io.realworldjpa.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class ArticleRestController {

    private final ArticleService articleService;

    @GetMapping("/{slug}")
    public ArticleRecord getArticle(User reader, @PathVariable String slug) {
        return new ArticleRecord(articleService.getArticle(reader, slug));
    }

    @GetMapping
    public MultipleArticleRecord getArticles(User reader, MultipleArticleRequest multipleArticleRequest) {
        return new MultipleArticleRecord(articleService.getArticles(reader, multipleArticleRequest));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/feed")
    public MultipleArticleRecord getFeedArticles(User reader, MultipleArticleRequest multipleArticleRequest) {
        return new MultipleArticleRecord(articleService.getFeedArticles(reader, multipleArticleRequest));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ArticleRecord postArticle(User author, @RequestBody ArticlePostRequest articlePostRequest) {
        return new ArticleRecord(articleService.createArticle(author, articlePostRequest));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{slug}")
    public ArticleRecord putArticle(User editor, @PathVariable String slug, @RequestBody ArticlePutRequest articlePutRequest) {
        return new ArticleRecord(articleService.updateArticle(editor, slug, articlePutRequest));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{slug}")
    public void deleteArticle(User author, @PathVariable String slug) {
        articleService.deleteArticle(author, slug);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{slug}/comments")
    public CommentRecord postComment(User author, @PathVariable String slug, @RequestBody CommentPostRequest commentPostRequest) {
        return new CommentRecord(articleService.createComment(author, slug, commentPostRequest));
    }

    @GetMapping("/{slug}/comments")
    public MultipleCommentRecord getComments(User reader, @PathVariable String slug) {
        return new MultipleCommentRecord(articleService.getArticleComments(reader, slug));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{slug}/comments/{id}")
    public void deleteComment(User author, @PathVariable String slug, @PathVariable(value = "id") long commentId) {
        articleService.deleteComment(author, commentId);
    }
}

package com.io.realworldjpa.domain.article.entity;

import com.io.realworldjpa.IntegrationTest;
import com.io.realworldjpa.domain.user.entity.Email;
import com.io.realworldjpa.domain.user.entity.Password;
import com.io.realworldjpa.domain.user.entity.Profile;
import com.io.realworldjpa.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@DisplayName("Article Entity Test")
class ArticleTest {

    private Article article;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setupUsers() throws Exception {
        User shimki = new User.Builder()
                .email(new Email("shimki@example.com"))
                .profile(Profile.of("shimki", "shimki's bio", "shimki.jpg"))
                .password(Password.of("testPassword", passwordEncoder))
                .build();

        article = new Article.Builder()
                .title("test Article title")
                .author(shimki)
                .content("test Content for Essential Body")
                .description("test Description")
                .build();
    }

    @Test
    @DisplayName("Add Tag To Article & Get Tag")
    void addTagForArticle() {
        // given
        Tag tag = new Tag("JPA");

        // when
        tag.addTagsToArticle(article);

        // then
        assertThat(article.getTags()).contains(tag);
    }

    @Test
    @DisplayName("Add TagList to Article & Get TagList")
    void tagList() {
        // given
        Tag java = new Tag("JAVA");
        Tag springframework = new Tag("SPRINGFRAMEWORK");
        Tag jacoco = new Tag("JACOCO");

        // when
        java.addTagsToArticle(article);
        springframework.addTagsToArticle(article);
        jacoco.addTagsToArticle(article);

        // then
        assertThat(article.getTags()).contains(java, springframework, jacoco);
    }

    @Test
    @DisplayName("Favorite Article.")
    void favorite() {
        User shimki = new User.Builder()
                .email(new Email("shimki@example.com"))
                .profile(Profile.of("shimki", "shimki's bio", "shimki.jpg"))
                .password(Password.of("testPassword", passwordEncoder))
                .build();

        // when
        shimki.favoriteArticle(article);

        // then
        assertThat(shimki.isAlreadyFavorite(article)).isTrue();
        assertThat(article.numberOfLikes()).isOne();
    }

    @Test
    @DisplayName("Unfavorite Article.")
    void unfavorite() {
        User shimki = new User.Builder()
                .email(new Email("shimki@example.com"))
                .profile(Profile.of("shimki", "shimki's bio", "shimki.jpg"))
                .password(Password.of("testPassword", passwordEncoder))
                .build();

        // when
        shimki.unfavoriteArticle(article);

        // then
        assertThat(shimki.isAlreadyFavorite(article)).isFalse();
        assertThat(article.numberOfLikes()).isZero();
    }
}
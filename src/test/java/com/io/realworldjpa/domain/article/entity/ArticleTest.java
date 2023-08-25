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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

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
                .body("test Content for Essential Body")
                .description("test Description")
                .build();
    }

    @Test
    @DisplayName("Add_Tag_To_Article & Get_Tag")
    void addTagForArticle() {
        // given
        Tag tag = new Tag("JPA");

        // when
        tag.addTagsToArticle(article);

        // then
        assertThat(article.getTags()).contains(tag);
    }

    @Test
    @DisplayName("Add_TagList_to_Article & Get_TagList")
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
    @DisplayName("Favorite_Article")
    void Favorite_Article() {
        User shimki = new User.Builder()
                .email(new Email("shimki@example.com"))
                .profile(Profile.of("shimki", "shimki's bio", "shimki.jpg"))
                .password(Password.of("testPassword", passwordEncoder))
                .build();

        // when
        shimki.favoriteArticle(article);

        // then
        assertThat(shimki.isAlreadyFavorite(article)).isTrue();
        assertThat(article.numberOfFavorites()).isOne();
        assertThat(article.getFavoriteUsers().stream().map(ArticleFavorite::getUser)).contains(shimki);
    }

    @Test
    @DisplayName("Favorite_Article_Repeat_Expect_Error")
    void Favorite_Repeat_Expect_Error() {
        User shimki = new User.Builder()
                .email(new Email("shimki@example.com"))
                .profile(Profile.of("shimki", "shimki's bio", "shimki.jpg"))
                .password(Password.of("testPassword", passwordEncoder))
                .build();

        // when
        shimki.favoriteArticle(article);
        assertThatThrownBy(() -> shimki.favoriteArticle(article))

                // then
                .isInstanceOf(IllegalArgumentException.class).hasMessage("이미 팔로우한 게시글입니다.");
    }

    @Test
    @DisplayName("Favorite_Not_Exist_Article_Expect_Error")
    void favorite_Not_Exist_Article_Expect_Error() {
        User shimki = new User.Builder()
                .email(new Email("shimki@example.com"))
                .profile(Profile.of("shimki", "shimki's bio", "shimki.jpg"))
                .password(Password.of("testPassword", passwordEncoder))
                .build();

        // when
        assertThatThrownBy(() -> shimki.favoriteArticle(null))

        // then
                .isInstanceOf(IllegalArgumentException.class).hasMessage("게시글이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("Unfavorite_Article")
    void Unfavorite_Article() {
        User shimki = new User.Builder()
                .email(new Email("shimki@example.com"))
                .profile(Profile.of("shimki", "shimki's bio", "shimki.jpg"))
                .password(Password.of("testPassword", passwordEncoder))
                .build();

        // when
        shimki.unfavoriteArticle(article);

        // then
        assertThat(shimki.isAlreadyFavorite(article)).isFalse();
        assertThat(article.numberOfFavorites()).isZero();
    }

    @Test
    @DisplayName("Unfavorite_Not_Exist_Article_Expect_Error")
    void Unfavorite_Not_Exist_Article_Expect_Error() {
        User shimki = new User.Builder()
                .email(new Email("shimki@example.com"))
                .profile(Profile.of("shimki", "shimki's bio", "shimki.jpg"))
                .password(Password.of("testPassword", passwordEncoder))
                .build();

        // when
        assertThatThrownBy(() -> shimki.unfavoriteArticle(null))

                // then
                .isInstanceOf(IllegalArgumentException.class).hasMessage("게시글이 존재하지 않습니다.");
    }
}
package com.io.realworldjpa.domain.article.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.io.realworldjpa.IntegrationTest;
import com.io.realworldjpa.domain.article.entity.Article;
import com.io.realworldjpa.domain.article.entity.Tag;
import com.io.realworldjpa.domain.article.model.ArticlePostRequest;
import com.io.realworldjpa.domain.article.model.ArticlePutRequest;
import com.io.realworldjpa.domain.article.service.ArticleRepository;
import com.io.realworldjpa.domain.article.service.ArticleService;
import com.io.realworldjpa.domain.user.entity.Email;
import com.io.realworldjpa.domain.user.entity.Password;
import com.io.realworldjpa.domain.user.entity.Profile;
import com.io.realworldjpa.domain.user.entity.User;
import com.io.realworldjpa.domain.user.model.LoginRequest;
import com.io.realworldjpa.domain.user.service.UserRepository;
import com.io.realworldjpa.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@DisplayName("ArticleAPI Test")
class ArticleRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private User shimki;
    private String shimkiToken;
    private User bangki;
    private User tanki;
    private String testSlug;

    @BeforeEach
    void setupUsers() throws Exception {
        shimki = createTestUsers("shimki");
        bangki = createTestUsers("bangki");
        tanki = createTestUsers("tanki");

        userRepository.save(shimki);
        userRepository.save(bangki);
        userRepository.save(tanki);

        tanki.followUser(shimki);
        tanki.followUser(bangki);
        shimki.followUser(tanki);

        for (int i = 0; i < 15; i++) {
            if (i < 7) {
                createTestArticle(shimki, i);
            }
            else if (i < 10) {
                createTestArticle(bangki, i);
            }
            else {
                createTestArticle(tanki, i);
            }
        }

        testSlug = "how-to-train-your-dragon";

        LoginRequest testLogin = new LoginRequest("shimki@example.com", "testPassword");
        shimkiToken = "Token " + userService.login(testLogin).token();
    }

    @Test
    @DisplayName("GET /api/articles/{slug}")
    void Get_Single_Article() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(get("/api/articles/{slug}", testSlug));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.article.title").value("How to train your dragon"))
                .andExpect(jsonPath("$.article.slug").value("how-to-train-your-dragon"))
                .andExpect(jsonPath("$.article.body").value("Ever wonder how?"))
                .andExpect(jsonPath("$.article.description").value("Very carefully."))
                .andExpect(jsonPath("$.article.tagList[0]").value("dragons"))
                .andExpect(jsonPath("$.article.author.username").value("shimki"))
                .andDo(print());
    }

    @Test
    @DisplayName("GET /api/articles")
    void Get_Multi_Articles() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(get("/api/articles"));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articlesCount").value(15))
                .andExpect(jsonPath("$.articles[0].slug").value("how-to-train-your-dragon-14"))
                .andExpect(jsonPath("$.articles[0].body").value("Ever wonder how? 14"))
                .andExpect(jsonPath("$.articles[0].description").value("Very carefully. 14"))
                .andExpect(jsonPath("$.articles[0].author.username").value("tanki"))
                .andExpect(jsonPath("$.articles[0].favorited").value(false))
                .andDo(print());
    }

    @Test
    @DisplayName("GET /api/articles - With Token")
    void Get_Multi_Articles_With_Token_Expect_OK() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(get("/api/articles")
                                                    .header("Authorization", shimkiToken));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articlesCount").value(15))
                .andExpect(jsonPath("$.articles[0].slug").value("how-to-train-your-dragon-14"))
                .andExpect(jsonPath("$.articles[0].body").value("Ever wonder how? 14"))
                .andExpect(jsonPath("$.articles[0].description").value("Very carefully. 14"))
                .andExpect(jsonPath("$.articles[0].author.username").value("tanki"))
                .andExpect(jsonPath("$.articles[0].favorited").value(true))
                .andDo(print());
    }

    @Test
    @DisplayName("GET /api/articles/feed")
    void Get_Feed_Articles() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(get("/api/articles/feed")
                .header("Authorization", shimkiToken));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articlesCount").value(5))
                .andExpect(jsonPath("$.articles[0].slug").value("how-to-train-your-dragon-14"))
                .andExpect(jsonPath("$.articles[0].body").value("Ever wonder how? 14"))
                .andExpect(jsonPath("$.articles[0].description").value("Very carefully. 14"))
                .andExpect(jsonPath("$.articles[0].author.username").value("tanki"))
                .andExpect(jsonPath("$.articles[0].favorited").value(true))
                .andDo(print());
    }

    @Test
    @DisplayName("GET /api/articles/feed - Without Token")
    void Get_Feed_Articles_Without_Token_Expect_Forbidden() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(get("/api/articles/feed"));

        // then
        resultActions
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @DisplayName("POST /api/articles")
    void Post_Article() throws Exception {
        //given
        ArticlePostRequest testPostRequest = new ArticlePostRequest("Test Title", "Test Description", "Test Body", List.of("Test Tag1", "Test Tag2", "Test Tag3"));

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/articles")
                                    .header("Authorization", shimkiToken)
                                    .content(objectMapper.writeValueAsString(Map.of("article", testPostRequest)))
                                    .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.article.title").value("Test Title"))
                .andExpect(jsonPath("$.article.slug").value("test-title"))
                .andExpect(jsonPath("$.article.body").value("Test Body"))
                .andExpect(jsonPath("$.article.description").value("Test Description"))
                .andExpect(jsonPath("$.article.tagList[0]").value("Test Tag1"))
                .andExpect(jsonPath("$.article.author.username").value("shimki"))
                .andDo(print());
    }

    @Test
    @DisplayName("POST /api/articles - Without Token")
    void Post_Article_Without_Token_Expect_UnAuthorized() throws Exception {
        //given
        ArticlePostRequest testPostRequest = new ArticlePostRequest("Test Title", "Test Description", "Test Body", List.of("Test Tag1", "Test Tag2", "Test Tag3"));

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/articles")
                .content(objectMapper.writeValueAsString(Map.of("article", testPostRequest)))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("PUT /api/articles/{slug}")
    void Put_Article() throws Exception {
        //given
        ArticlePutRequest testPutRequest = new ArticlePutRequest("Put Title", "Put Description", "Put Body");

        // when
        ResultActions resultActions = mockMvc.perform(put("/api/articles/{slug}", testSlug)
                .header("Authorization", shimkiToken)
                .content(objectMapper.writeValueAsString(Map.of("article", testPutRequest)))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.article.title").value("Put Title"))
                .andExpect(jsonPath("$.article.slug").value("put-title"))
                .andExpect(jsonPath("$.article.body").value("Put Body"))
                .andExpect(jsonPath("$.article.description").value("Put Description"))
                .andExpect(jsonPath("$.article.author.username").value("shimki"))
                .andDo(print());
    }

    @Test
    @DisplayName("PUT /api/articles/{slug} - Without Token")
    void Put_Article_Without_Token_Expect_UnAuthorized() throws Exception {
        //given
        ArticlePutRequest testPutRequest = new ArticlePutRequest("Put Title", "Put Description", "Put Body");

        // when
        ResultActions resultActions = mockMvc.perform(put("/api/articles/{slug}", testSlug)
                .content(objectMapper.writeValueAsString(Map.of("article", testPutRequest)))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("DELETE /api/articles/{slug}")
    void Delete_Article() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/articles/{slug}", testSlug)
                .header("Authorization", shimkiToken)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("DELETE /api/articles/{slug} - Without Token")
    void Delete_Article_Without_Token_Expect_UnAuthorized() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(put("/api/articles/{slug}", testSlug)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    private User createTestUsers(String testName) {
        return new User.Builder()
                .email(new Email(testName + "@example.com"))
                .profile(Profile.of(testName, testName + "'s bio", testName + ".jpg"))
                .password(Password.of("testPassword", passwordEncoder))
                .build();
    }

    private void createTestArticle(User author, int i) {
        Article testArticle = null;
        if (i == 0) {
            testArticle = new Article.Builder()
                    .title("How to train your dragon")
                    .description("Very carefully.")
                    .content("Ever wonder how?")
                    .author(author)
                    .build();
            Tag tag1 = new Tag("training");
            Tag tag2 = new Tag("dragons");

            testArticle.addTag(tag1);
            testArticle.addTag(tag2);
            bangki.favoriteArticle(testArticle);
        }
        else {
            testArticle = new Article.Builder()
                    .title("%s %d".formatted("How to train your dragon", i))
                    .description("%s %d".formatted("Very carefully.", i))
                    .content("%s %d".formatted("Ever wonder how?", i))
                    .author(author)
                    .build();
            if (i > 7) {
                shimki.favoriteArticle(testArticle);
            }
        }

        articleRepository.save(testArticle);
    }

}
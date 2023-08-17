package com.io.realworldjpa.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.io.realworldjpa.IntegrationTest;
import com.io.realworldjpa.domain.user.model.LoginRequest;
import com.io.realworldjpa.domain.user.model.UserPostRequest;
import com.io.realworldjpa.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@IntegrationTest
@DisplayName("ProfileAPI Test")
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;

    @BeforeEach
    void setupUsers() throws Exception {
        userService.signUp(new UserPostRequest("shimki@example.com", "shimki", "testPassword", "shimki's bio", "shimki.jpg"));
        userService.signUp(new UserPostRequest("bangki@example.com", "bangki", "testPassword", "bangki's bio", "bangki.jpg"));
    }

    @Test
    @DisplayName("GET profiles - WithoutAuth")
    public void getProfile_without_Auth_expect_OK() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(get("/api/profiles/{username}", "bangki"));
        
        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.profile.username").value("bangki"))
                .andExpect(jsonPath("$.profile.bio").value("bangki's bio"))
                .andExpect(jsonPath("$.profile.image").value("bangki.jpg"))
                .andExpect(jsonPath("$.profile.following").value(false))
                .andDo(print());
    }

    @Test
    @DisplayName("GET profiles - WithAuth")
    public void getProfile_with_Auth_expect_OK() throws Exception {
        //given
        LoginRequest loginRequest = new LoginRequest("shimki@example.com", "testPassword");
        String testToken = userService.login(loginRequest).token();

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/profiles/{username}", "bangki")
                        .header("Authorization", "Token " + testToken));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.profile.username").value("bangki"))
                .andExpect(jsonPath("$.profile.bio").value("bangki's bio"))
                .andExpect(jsonPath("$.profile.image").value("bangki.jpg"))
                .andExpect(jsonPath("$.profile.following").value(false))
                .andDo(print());
    }

    @Test
    @DisplayName("POST follow - WithAuth")
    public void follow_with_Auth_expect_OK() throws Exception {
        //given
        LoginRequest loginRequest = new LoginRequest("shimki@example.com", "testPassword");
        String testToken = userService.login(loginRequest).token();

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/profiles/{username}/follow", "bangki")
                        .header("Authorization", "Token " + testToken));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.profile.username").value("bangki"))
                .andExpect(jsonPath("$.profile.bio").value("bangki's bio"))
                .andExpect(jsonPath("$.profile.image").value("bangki.jpg"))
                .andExpect(jsonPath("$.profile.following").value(true))
                .andDo(print());
    }

    @Test
    @DisplayName("POST follow - WithoutAuth")
    public void follow_without_Auth_expect_UnAuthorized() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/profiles/{username}/follow", "bangki"));

        // then
        resultActions
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("DELETE follow - WithAuth")
    public void unfollow_with_Auth_expect_OK() throws Exception {
        //given
        LoginRequest loginRequest = new LoginRequest("shimki@example.com", "testPassword");
        String testToken = userService.login(loginRequest).token();

        // when
        ResultActions resultActions = mockMvc.perform(
                delete("/api/profiles/{username}/follow", "bangki")
                        .header("Authorization", "Token " + testToken));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.profile.username").value("bangki"))
                .andExpect(jsonPath("$.profile.bio").value("bangki's bio"))
                .andExpect(jsonPath("$.profile.image").value("bangki.jpg"))
                .andExpect(jsonPath("$.profile.following").value(false))
                .andDo(print());
    }

    @Test
    @DisplayName("DELETE follow - WithoutAuth")
    public void unfollow_without_Auth_expect_UnAuthorized() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(
                delete("/api/profiles/{username}/follow", "bangki"));

        // then
        resultActions
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
}
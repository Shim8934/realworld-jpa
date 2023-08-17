package com.io.realworldjpa.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.io.realworldjpa.IntegrationTest;
import com.io.realworldjpa.domain.user.model.LoginRequest;
import com.io.realworldjpa.domain.user.model.UserPostRequest;
import com.io.realworldjpa.domain.user.model.UserPutRequest;
import com.io.realworldjpa.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Map;
import java.util.stream.Stream;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTest
@DisplayName("UserAPI Test")
class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;

    @Test
    @DisplayName("POST /api/users")
    void post_registUser() throws Exception {
        // given
        UserPostRequest userPostRequest = new UserPostRequest("testEmail10@example.com", "testUsername10", "testPassword", "", "");

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userPostRequest)));

        // then
        resultActions
                .andExpect(status().isTemporaryRedirect())
                .andExpect(view().name("redirect:/api/users/login"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", Map.of("user", new LoginRequest("testEmail10@example.com", "testPassword"))))
                .andDo(print());
    }
    @ParameterizedTest
    @MethodSource("invalidPostRequest")
    @DisplayName("POST /api/users/login - Invalid Request")
    void post_user_with_invalid_request_expect_badRequest_status(UserPostRequest userPostRequest) throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userPostRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/users/login")
    void post_loginUser() throws Exception {
        // given
        UserPostRequest userPostRequest = new UserPostRequest("testEmail10@example.com", "testUsername10", "testPassword", "", "");
        userService.signUp(userPostRequest);

        // when
        LoginRequest loginRequest = new LoginRequest("testEmail10@example.com", "testPassword");

        // then
        ResultActions resultActions = mockMvc.perform(
                post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(loginRequest))
        );

        resultActions
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.user.email").value("testEmail10@example.com"))
                .andExpect(jsonPath("$.user.username").value("testUsername10"))
                .andExpect(jsonPath("$.user.token").isNotEmpty())
                .andExpect(jsonPath("$.user.bio").isEmpty())
                .andExpect(jsonPath("$.user.image").isEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("GET /api/user")
    void get_User() throws Exception {
        // given
        UserPostRequest userPostRequest = new UserPostRequest("testEmail10@example.com", "testUsername10", "testPassword", "", "");
        userService.signUp(userPostRequest);

        LoginRequest loginRequest = new LoginRequest("testEmail10@example.com", "testPassword");
        String testToken = userService.login(loginRequest).token();

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/user").header("Authorization", "Token " + testToken));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.user.email").value("testEmail10@example.com"))
                .andExpect(jsonPath("$.user.username").value("testUsername10"))
                .andExpect(jsonPath("$.user.token").isNotEmpty())
                .andExpect(jsonPath("$.user.bio").isEmpty())
                .andExpect(jsonPath("$.user.image").isEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("PUT /api/user")
    void put_User() throws Exception {
        // given
        UserPostRequest userPostRequest = new UserPostRequest("testEmail10@example.com", "testUsername10", "testPassword", "", "");
        userService.signUp(userPostRequest);

        LoginRequest loginRequest = new LoginRequest("testEmail10@example.com", "testPassword");
        String testToken = userService.login(loginRequest).token();

        // when
        UserPutRequest userPutRequest = new UserPutRequest("testEmail@example.com", "testUsername10", "testPassword", "testBio", "testImage.url");
        ResultActions resultActions = mockMvc.perform(put("/api/user").header("Authorization", "Token " + testToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userPutRequest)));


        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.user.email").value("testEmail@example.com"))
                .andExpect(jsonPath("$.user.username").value("testUsername10"))
                .andExpect(jsonPath("$.user.token").isNotEmpty())
                .andExpect(jsonPath("$.user.bio").value("testBio"))
                .andExpect(jsonPath("$.user.image").value("testImage.url"))
                .andDo(print());
    }

    @Test
    @DisplayName("PUT /api/user - Same Email")
    public void put_User_withSameEmail_expect_NotEmailUpdate() throws Exception {
        // given
        UserPostRequest userPostRequest = new UserPostRequest("testEmail10@example.com", "testUsername10", "testPassword", "", "");
        userService.signUp(userPostRequest);

        LoginRequest loginRequest = new LoginRequest("testEmail10@example.com", "testPassword");
        String testToken = userService.login(loginRequest).token();

        // when
        UserPutRequest userPutRequest = new UserPutRequest("testEmail10@example.com", "testUsername10", "testPassword", "testBio", "testImage.url");
        ResultActions resultActions = mockMvc.perform(put("/api/user").header("Authorization", "Token " + testToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userPutRequest)));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.user.email").value("testEmail10@example.com"))
                .andExpect(jsonPath("$.user.username").value("testUsername10"))
                .andExpect(jsonPath("$.user.token").isNotEmpty())
                .andExpect(jsonPath("$.user.bio").value("testBio"))
                .andExpect(jsonPath("$.user.image").value("testImage.url"))
                .andDo(print());
    }

    private static Stream<Arguments> invalidPostRequest() {
        return Stream.of(
                Arguments.of(new UserPostRequest("not-email", "username", "password", "", "")),
                Arguments.of(new UserPostRequest("user@email.com", "", "password", "", "")),
                Arguments.of(new UserPostRequest("user@email.com", "username", "", "", ""))
        );
    }
}
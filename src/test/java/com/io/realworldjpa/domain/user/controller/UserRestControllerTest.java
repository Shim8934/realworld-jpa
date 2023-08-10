package com.io.realworldjpa.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.io.realworldjpa.IntegrationTest;
import com.io.realworldjpa.domain.security.service.JwtDecoder;
import com.io.realworldjpa.domain.security.service.JwtEncoder;
import com.io.realworldjpa.domain.user.model.LoginRequest;
import com.io.realworldjpa.domain.user.model.UserPostRequest;
import com.io.realworldjpa.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTest
@DisplayName("UserAPI Test")
class UserRestControllerTest {

    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private JwtDecoder jwtDecoder;

    @Test
    @DisplayName("POST /api/users")
    void post_registUser() throws Exception {
        // given
        UserPostRequest userPostRequest = new UserPostRequest("testUsername5", "testEmail5@example.com", "testPassword");

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userPostRequest)));

        // then
        resultActions
                .andExpect(status().isTemporaryRedirect())
                .andExpect(view().name("redirect:/api/users/login"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", Map.of("user", new LoginRequest("testEmail5@example.com", "testPassword"))))
                .andDo(print());
    }

    @Test
    @DisplayName("POST /api/users/login")
    void post_loginUser() throws Exception {
        UserPostRequest userPostRequest = new UserPostRequest("testUsername10", "testEmail10@example.com", "testPassword");
        userService.signUp(userPostRequest);

        LoginRequest loginRequest = new LoginRequest("testEmail10@example.com", "testPassword");

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


}
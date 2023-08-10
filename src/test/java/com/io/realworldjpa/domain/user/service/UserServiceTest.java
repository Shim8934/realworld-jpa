package com.io.realworldjpa.domain.user.service;

import com.io.realworldjpa.IntegrationTest;
import com.io.realworldjpa.domain.user.entity.User;
import com.io.realworldjpa.domain.user.model.LoginRequest;
import com.io.realworldjpa.domain.user.model.UserPostRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
@DisplayName("UserService 테스트")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("신규 유저 등록 서비스")
    void signUp() throws Exception {
        // given
        UserPostRequest userPostRequest = new UserPostRequest("testUsername10", "testEmail10@example.com", "testPassword");

        // when
        User user = userService.signUp(userPostRequest);

        // then
        assertThat(user.getId()).isNotNull();
        assertThat(user.getEmail().getAddress()).isEqualTo("testEmail10@example.com");
        assertThat(user.getProfile().getUsername()).isEqualTo("testUsername10");
        assertThat(user.getPassword()).isNotEqualTo("testPassword");
    }

    @Test
    @DisplayName("유저 로그인 서비스")
    void login() throws Exception {
        // given
        UserPostRequest userPostRequest = new UserPostRequest("testUsername10", "testEmail10@example.com", "testPassword");
        userService.signUp(userPostRequest);

        // when
        User user = userService.login(new LoginRequest("testEmail10@example.com", "testPassword"));

        // then
        assertThat(user.getEmail().getAddress()).isEqualTo("testEmail10@example.com");
        assertThat(user.getProfile().getUsername()).isEqualTo("testUsername10");
        assertThat(user.getProfile().getBio()).isNull();
        assertThat(user.getProfile().getImage()).isNull();
    }

}
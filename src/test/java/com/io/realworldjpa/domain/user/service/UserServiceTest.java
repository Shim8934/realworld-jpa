package com.io.realworldjpa.domain.user.service;

import com.io.realworldjpa.IntegrationTest;
import com.io.realworldjpa.domain.user.entity.Password;
import com.io.realworldjpa.domain.user.entity.User;
import com.io.realworldjpa.domain.user.model.LoginRequest;
import com.io.realworldjpa.domain.user.model.UserPostRequest;
import com.io.realworldjpa.domain.user.model.UserPutRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@DisplayName("UserService Test")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("UserService SignUp")
    void signUp() throws Exception {
        // given
        UserPostRequest userPostRequest = new UserPostRequest("testEmail10@example.com", "testUsername10", "testPassword");

        // when
        User user = userService.signUp(userPostRequest);

        // then
        assertThat(user.getId()).isNotNull();
        assertThat(user.getEmail().getAddress()).isEqualTo("testEmail10@example.com");
        assertThat(user.getProfile().getUsername()).isEqualTo("testUsername10");
        assertThat(user.getPassword()).isNotEqualTo(Password.of("testPassword", passwordEncoder));
    }

    @Test
    @DisplayName("UserService Login")
    void login() throws Exception {
        // given
        UserPostRequest userPostRequest = new UserPostRequest("testEmail10@example.com", "testUsername10", "testPassword");
        userService.signUp(userPostRequest);

        // when
        User user = userService.login(new LoginRequest("testEmail10@example.com", "testPassword"));

        // then
        assertThat(user.getEmail().getAddress()).isEqualTo("testEmail10@example.com");
        assertThat(user.getProfile().getUsername()).isEqualTo("testUsername10");
        assertThat(user.getProfile().getBio()).isNull();
        assertThat(user.getProfile().getImage()).isNull();
    }

    @Test
    @DisplayName("UserService Update")
    void updateUser() throws Exception {
        // given
        UserPostRequest userPostRequest = new UserPostRequest("testEmail10@example.com", "testUsername10", "testPassword");

        // when
        User user = userService.signUp(userPostRequest);
        UserPutRequest userPutRequest = new UserPutRequest("testEmail10@example.com", "testUsername10", "testPassword", "testBio", "testImage.url");
        User updateUser = userService.updateUser(user, userPutRequest);

        // then
        assertThat(updateUser.getEmail().getAddress()).isEqualTo("testEmail10@example.com");
        assertThat(updateUser.getProfile().getUsername()).isEqualTo("testUsername10");
        assertThat(updateUser.getProfile().getBio()).isEqualTo("testBio");
        assertThat(updateUser.getProfile().getImage()).isEqualTo("testImage.url");
    }
}
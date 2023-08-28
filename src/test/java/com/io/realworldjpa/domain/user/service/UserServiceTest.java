package com.io.realworldjpa.domain.user.service;

import com.io.realworldjpa.IntegrationTest;
import com.io.realworldjpa.domain.user.entity.*;
import com.io.realworldjpa.domain.user.model.LoginRequest;
import com.io.realworldjpa.domain.user.model.UserPostRequest;
import com.io.realworldjpa.domain.user.model.UserPutRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@IntegrationTest
@DisplayName("UserService Test")
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private User shimki;

    @BeforeEach
    void setupUsers() throws Exception {
        shimki = new User.Builder()
                .email(Email.of("shimki@example.com"))
                .profile(Profile.of("shimki", "shimki's bio", "shimki.jpg"))
                .password(Password.of("testPassword", passwordEncoder))
                .build();

        userRepository.save(shimki);
    }

    @Test
    @DisplayName("SignUp")
    void signUp() throws Exception {
        // given
        UserPostRequest userPostRequest = new UserPostRequest("testEmail10@example.com", "testUsername10", "testPassword", "", "");

        // when
        User user = userService.signUp(userPostRequest);

        // then
        assertThat(user.getId()).isNotNull();
        assertThat(user.getEmail().getAddress()).isEqualTo("testEmail10@example.com");
        assertThat(user.getProfile().getUsername()).isEqualTo("testUsername10");
        assertThat(user.getPassword()).isNotEqualTo(Password.of("testPassword", passwordEncoder));
    }

    @Test
    @DisplayName("SignUp_Exist_Email")
    void signUp_With_Exist_Email_Expect_Error() throws Exception {
        // given
        UserPostRequest userPostRequest = new UserPostRequest("shimki@example.com", "shimki", "testPassword", "shimki's bio", "shimki.jpg");

        // when
        assertThatThrownBy(() -> userService.signUp(userPostRequest))
        // then
                .isInstanceOf(IllegalArgumentException.class)
        ;
    }

    @Test
    @DisplayName("Login")
    void login() throws Exception {
        // given
        UserPostRequest userPostRequest = new UserPostRequest("testEmail10@example.com", "testUsername10", "testPassword", "", "");
        userService.signUp(userPostRequest);

        // when
        UserDto testUser = userService.login(new LoginRequest("testEmail10@example.com", "testPassword"));

        // then
        assertThat(testUser.email()).isEqualTo("testEmail10@example.com");
        assertThat(testUser.username()).isEqualTo("testUsername10");
        assertThat(testUser.bio()).isBlank();
        assertThat(testUser.image()).isBlank();
    }

    @Test
    @DisplayName("Update")
    void updateUser() throws Exception {
        // given
        UserPostRequest userPostRequest = new UserPostRequest("testEmail10@example.com", "testUsername10", "testPassword", "", "");

        // when
        User user = userService.signUp(userPostRequest);
        UserPutRequest userPutRequest = new UserPutRequest("testEmail10@example.com", "testUsername10", "testPassword", "testBio", "testImage.url");
        UserDto updateUser = userService.updateUser(user, userPutRequest);

        // then
        assertThat(updateUser.email()).isEqualTo("testEmail10@example.com");
        assertThat(updateUser.username()).isEqualTo("testUsername10");
        assertThat(updateUser.bio()).isEqualTo("testBio");
        assertThat(updateUser.image()).isEqualTo("testImage.url");
    }

    @Test
    @DisplayName("Update_Exist_Email")
    void update_With_Exist_Email_Expect_Error() throws Exception {
        // given
        UserPostRequest userPostRequest = new UserPostRequest("testEmail10@example.com", "testUsername10", "testPassword", "", "");
        User user = userService.signUp(userPostRequest);

        UserPutRequest userPutRequest = new UserPutRequest("shimki@example.com", "shimki", "testPassword", "shimki's bio", "shimki.jpg");

        // when
        assertThatThrownBy(() -> userService.updateUser(user, userPutRequest))

        // then
                .isInstanceOf(IllegalArgumentException.class)
        ;
    }
}
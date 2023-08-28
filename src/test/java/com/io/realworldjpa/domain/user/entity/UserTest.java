package com.io.realworldjpa.domain.user.entity;

import com.io.realworldjpa.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@IntegrationTest
@DisplayName("User Entity Test")
class UserTest {

    private User shimki;
    private User bangki;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setupUsers() throws Exception {
        shimki = new User.Builder()
                .email(Email.of("shimki@example.com"))
                .profile(Profile.of("shimki", "shimki's bio", "shimki.jpg"))
                .password(Password.of("testPassword", passwordEncoder))
                .build();

        bangki = new User.Builder()
                .email(Email.of("bangki@example.com"))
                .profile(Profile.of("bangki", "bangki's bio", "bangki.jpg"))
                .password(Password.of("testPassword", passwordEncoder))
                .build();
    }

    @Test
    @DisplayName("User_Follow_Test")
    void followUser() throws Exception {
        // given
        shimki.followUser(bangki);

        // when
        User getFollowUser = shimki.getFollowing().stream().map(Follow::getTo).findFirst().get();
        User getFollowingUser = bangki.getFollower().stream().map(Follow::getFrom).findFirst().get();

        // then
        assertThat(getFollowUser).isEqualTo(bangki);

        assertThat(getFollowingUser).isEqualTo(shimki);
        assertThat(getFollowingUser.getId()).isEqualTo(getFollowUser.getId());

        assertThat(shimki.followUserList()).contains(bangki);
    }

    @Test
    @DisplayName("Follow_Repeat_Expect_Error")
    void FollowUser_Repeat_Expect_Error() throws Exception {
        // given
        shimki.followUser(bangki);

        // when
        assertThatThrownBy(() -> shimki.followUser(bangki))

        // then
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("User_Unfollow_Test")
    void unFollowUser() throws Exception {
        // given
        shimki.followUser(bangki);

        // when
        shimki.unfollowUser(bangki);

        // then
        assertThatThrownBy(() -> shimki.getFollowing().stream().map(Follow::getTo).findFirst().get())
                .isInstanceOf(NoSuchElementException.class);
    }
}
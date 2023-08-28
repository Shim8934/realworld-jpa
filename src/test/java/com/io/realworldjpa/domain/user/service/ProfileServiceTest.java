package com.io.realworldjpa.domain.user.service;

import com.io.realworldjpa.IntegrationTest;
import com.io.realworldjpa.domain.user.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@IntegrationTest
@DisplayName("ProfileService Test")
class ProfileServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private User shimki;
    private User bangki;

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

        userRepository.save(shimki);
        userRepository.save(bangki);
    }

    @Test
    @DisplayName("Get_Profile")
    void getProfile() throws Exception {
        // given
        shimki = userRepository.findFirstByEmail(Email.of("shimki@example.com")).orElseThrow();

        // when
        ProfileDto profileOfBangki = profileService.getProfile(shimki, "bangki");

        // then
        assertThat(profileOfBangki.username()).isEqualTo("bangki");
        assertThat(profileOfBangki.bio()).isEqualTo("bangki's bio");
        assertThat(profileOfBangki.image()).isEqualTo("bangki.jpg");
        assertThat(profileOfBangki.following()).isFalse();
    }

    @Test
    @DisplayName("Get_Profile_Not_Exist_User")
    void getProfile_Not_Exist_User() throws Exception {
        // given
        shimki = userRepository.findFirstByEmail(Email.of("shimki@example.com")).orElseThrow();

        // when
        assertThatThrownBy(() -> profileService.getProfile(shimki, "not_existed_user"))

        // then
                .isInstanceOf(NoSuchElementException.class)
        ;
    }

    @Test
    @DisplayName("Follow")
    void follow_Profile() throws Exception {
        // given
        shimki = userRepository.findFirstByEmail(Email.of("shimki@example.com")).orElseThrow();

        // when
        ProfileDto profileOfBangki = profileService.follow(shimki, "bangki");

        // then
        assertThat(profileOfBangki.username()).isEqualTo("bangki");
        assertThat(profileOfBangki.bio()).isEqualTo("bangki's bio");
        assertThat(profileOfBangki.image()).isEqualTo("bangki.jpg");
        assertThat(profileOfBangki.following()).isTrue();

        assertThat(shimki.isAlreadyFollow(bangki)).isTrue();
    }

    @Test
    @DisplayName("Follow_Not_Exist_User")
    void follow_Not_Exist_User() throws Exception {
        // given
        shimki = userRepository.findFirstByEmail(Email.of("shimki@example.com")).orElseThrow();

        // when
        assertThatThrownBy(() -> profileService.follow(shimki, "notUser"))

        // then
                .isInstanceOf(NoSuchElementException.class);
        assertThat(shimki.isAlreadyFollow(bangki)).isFalse();
    }

    @Test
    @DisplayName("Unfollow")
    void unFollowProfile() throws Exception {
        // given
        shimki = userRepository.findFirstByEmail(Email.of("shimki@example.com")).orElseThrow();

        // when
        ProfileDto profileOfBangki = profileService.unfollow(shimki, "bangki");

        // then
        assertThat(profileOfBangki.username()).isEqualTo("bangki");
        assertThat(profileOfBangki.bio()).isEqualTo("bangki's bio");
        assertThat(profileOfBangki.image()).isEqualTo("bangki.jpg");
        assertThat(profileOfBangki.following()).isFalse();
    }

    @Test
    @DisplayName("Unfollow_Not_Exist_User")
    void unFollow_Not_Exist_User() throws Exception {
        // given
        shimki = userRepository.findFirstByEmail(Email.of("shimki@example.com")).orElseThrow();

        // when
        assertThatThrownBy(() -> profileService.follow(shimki, "notUser"))

                // then
                .isInstanceOf(NoSuchElementException.class);
    }
}
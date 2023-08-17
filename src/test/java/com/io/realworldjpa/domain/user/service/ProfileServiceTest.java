package com.io.realworldjpa.domain.user.service;

import com.io.realworldjpa.IntegrationTest;
import com.io.realworldjpa.domain.user.entity.*;
import com.io.realworldjpa.domain.user.model.UserPostRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
@DisplayName("ProfileService Test")
class ProfileServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setupUsers() throws Exception {
        User shimki = new User.Builder()
                .email(new Email("shimki@example.com"))
                .profile(Profile.of("shimki", "shimki's bio", "shimki.jpg"))
                .password(Password.of("testPassword", passwordEncoder))
                .build();

        User bangki = new User.Builder()
                .email(new Email("bangki@example.com"))
                .profile(Profile.of("bangki", "bangki's bio", "bangki.jpg"))
                .password(Password.of("testPassword", passwordEncoder))
                .build();

        userRepository.save(shimki);
        userRepository.save(bangki);
    }

    @Test
    @DisplayName("ProfileService getProfile")
    void getProfile() throws Exception {
        // given
        User shimki = userRepository.findFirstByEmail(new Email("shimki@example.com")).orElseThrow();

        // when
        ProfileDto profileOfBangki = profileService.getProfile(shimki, "bangki");

        // then
        assertThat(profileOfBangki.username()).isEqualTo("bangki");
        assertThat(profileOfBangki.bio()).isEqualTo("bangki's bio");
        assertThat(profileOfBangki.image()).isEqualTo("bangki.jpg");
        assertThat(profileOfBangki.following()).isFalse();
    }

    @Test
    @DisplayName("ProfileService follow")
    void followProfile() throws Exception {
        // given
        User shimki = userRepository.findFirstByEmail(new Email("shimki@example.com")).orElseThrow();

        // when
        ProfileDto profileOfBangki = profileService.follow(shimki, "bangki");

        // then
        assertThat(profileOfBangki.username()).isEqualTo("bangki");
        assertThat(profileOfBangki.bio()).isEqualTo("bangki's bio");
        assertThat(profileOfBangki.image()).isEqualTo("bangki.jpg");
        assertThat(profileOfBangki.following()).isTrue();
    }

    @Test
    @DisplayName("ProfileService unfollow")
    void unFollowProfile() throws Exception {
        // given
        User shimki = userRepository.findFirstByEmail(new Email("shimki@example.com")).orElseThrow();

        // when
        ProfileDto profileOfBangki = profileService.unfollow(shimki, "bangki");

        // then
        assertThat(profileOfBangki.username()).isEqualTo("bangki");
        assertThat(profileOfBangki.bio()).isEqualTo("bangki's bio");
        assertThat(profileOfBangki.image()).isEqualTo("bangki.jpg");
        assertThat(profileOfBangki.following()).isFalse();
    }

}
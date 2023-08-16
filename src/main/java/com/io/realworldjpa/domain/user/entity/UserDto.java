package com.io.realworldjpa.domain.user.entity;

public record UserDto(String email, String username, String token, String bio, String image) {

    public UserDto(User user, String token) {
        this(user.getEmail().getAddress(), user.getProfile().getUsername(), token, user.getProfile().getBio(), user.getProfile().getImage());
    }

}

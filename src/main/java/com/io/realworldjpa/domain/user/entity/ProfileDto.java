package com.io.realworldjpa.domain.user.entity;

public record ProfileDto(String username, String bio, String image, boolean following) {

    public ProfileDto(User from, User target) {
        this(target.getProfile().getUsername(), target.getProfile().getBio(), target.getProfile().getImage(), from != null && from.isAlreadyFollow(target));
    }
}

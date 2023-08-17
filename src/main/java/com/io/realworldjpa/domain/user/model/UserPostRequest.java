package com.io.realworldjpa.domain.user.model;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("user")
public record UserPostRequest(String email, String username, String password, String bio, String image) {

    public UserPostRequest {
        if (bio == null) {
            bio = "";
        }
        if (image == null) {
            image = "";
        }
    }

}

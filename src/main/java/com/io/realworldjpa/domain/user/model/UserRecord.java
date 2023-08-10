package com.io.realworldjpa.domain.user.model;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.io.realworldjpa.domain.user.entity.User;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;
@JsonRootName("user")
public record UserRecord(String email, String username, String token, String bio, String image) {

    public UserRecord(User user, String token) {
        this(user.getEmail().getAddress(), user.getProfile().getUsername(), token, user.getProfile().getBio(), user.getProfile().getImage());
    }

}

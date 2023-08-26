package com.io.realworldjpa.domain.user.model;


import com.fasterxml.jackson.annotation.JsonRootName;


@JsonRootName("user")
public record UserPutRequest(String email, String username, String password, String bio, String image) {}

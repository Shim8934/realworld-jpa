package com.io.realworldjpa.domain.user.model;


import com.fasterxml.jackson.annotation.JsonRootName;


@JsonRootName("user")
public record LoginRequest(String email, String password) {}

package com.io.realworldjpa.domain.user.model;


import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.io.realworldjpa.domain.user.entity.ProfileDto;

@JsonRootName("profile")
public record ProfileRecord(@JsonValue ProfileDto profileDto) {}

package com.io.realworldjpa.domain.user.model;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.io.realworldjpa.domain.user.entity.ProfileDto;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeName("profile")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = NAME)
public record ProfileRecord(@JsonValue ProfileDto profileDto) {}

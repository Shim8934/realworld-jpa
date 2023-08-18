package com.io.realworldjpa.domain.user.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.io.realworldjpa.domain.user.entity.UserDto;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeName("user")
@JsonTypeInfo(include = As.WRAPPER_OBJECT, use = NAME)
public record UserRecord(@JsonValue UserDto user) {}

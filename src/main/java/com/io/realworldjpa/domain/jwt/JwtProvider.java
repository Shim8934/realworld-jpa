package com.io.realworldjpa.domain.jwt;

import com.io.realworldjpa.domain.user.entity.User;
import com.nimbusds.jose.JOSEException;

public interface JwtProvider {

    String jwtFromUser(User user) throws JOSEException;

}

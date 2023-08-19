package com.io.realworldjpa.global.security;

import com.io.realworldjpa.domain.jwt.JwtProvider;
import com.io.realworldjpa.domain.user.entity.User;
import com.io.realworldjpa.global.util.Generated;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import static java.time.Duration.*;
import static java.time.temporal.ChronoUnit.MINUTES;

@Slf4j
@Generated
@RequiredArgsConstructor
public class JwtCustomProvider implements JwtProvider {
    private final byte[] secretKey;
    private final String issuer;
    private final long expMinute;

    @Override
    public String jwtFromUser(User user) throws JOSEException {
        JWSSigner signer = new MACSigner(secretKey);

        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();

        Date currentTime = new Date();
        Duration expirationTime = ofMinutes(expMinute);

        JWTClaimsSet claimsSet = builder
                .issuer(issuer)
                .issueTime(currentTime)
                .expirationTime(expirationTime == ZERO ? null : Date.from(Instant.now().plus(expMinute, MINUTES)))
                .claim("name", user.getEmail().getAddress())
                .subject(user.getId().toString())
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.HS512)
                .type(JOSEObjectType.JWT)
                .build(), claimsSet);

        signedJWT.sign(signer);
        String serializeToken = signedJWT.serialize();

        log.debug("JWT 생성 = ID - `{}`  : Token - {}", user.getId(), serializeToken);

        return serializeToken;
    }

}

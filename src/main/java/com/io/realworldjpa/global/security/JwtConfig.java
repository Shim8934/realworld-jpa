package com.io.realworldjpa.global.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "realworld.jwt")
public class JwtConfig {

    private String secretKey;
    private String issuer;
    private long expMinute;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public long getExpMinute() {
        return expMinute;
    }

    public void setExpMinute(long expMinute) {
        this.expMinute = expMinute;
    }

}

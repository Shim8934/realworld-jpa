package com.io.realworldjpa.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.springframework.security.crypto.password.PasswordEncoder;

@Embeddable
public class Password {

    @Column(name = "password", nullable = false)
    private String password;

    public static Password of(String rawPassword, PasswordEncoder passwordEncoder) {
        return new Password(passwordEncoder.encode(rawPassword));
    }

    private Password(String encodedPassword) {
        this.password = encodedPassword;
    }

    protected Password() {
    }

    boolean matchesPassword(String rawPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(rawPassword, password);
    }

}

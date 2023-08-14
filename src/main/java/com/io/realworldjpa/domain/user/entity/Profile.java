package com.io.realworldjpa.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@Embeddable
public class Profile {

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "bio")
    private String bio;

    @Column(name = "image")
    private String image;

    protected Profile() {}

    private Profile(String username, String bio, String image) {
        this.username = defaultIfNull(username, "");
        this.bio = bio;
        this.image = image;
    }

    public Profile(String username) {
        this(username, null, null);
    }

    public String getUsername() {
        return username;
    }

    public String getBio() {
        return bio;
    }

    public String getImage() {
        return image;
    }

    public void updateUsername(String username) {
        this.username = username;
    }

    public void updateBio(String bio) {
        this.bio = bio;
    }

    public void updateImage(String image) {
        this.image = image;
    }
}

package com.io.realworldjpa.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.*;

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
        checkArgument(isNotBlank(username), "username must not be null or blank!");
        this.username = username;
        this.bio = bio;
        this.image = image;
    }

    public static Profile of(String username, String bio, String image) {
        return new Profile(username, bio, image);
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

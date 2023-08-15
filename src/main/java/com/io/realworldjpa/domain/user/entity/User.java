package com.io.realworldjpa.domain.user.entity;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.GenerationType.IDENTITY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@RequiredArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Embedded
    private Email email;

    @Embedded
    private Profile profile;

    @Embedded
    private Password password;

    @Transient
    private String token;

    @JoinTable(name = "users_followings",
            joinColumns = @JoinColumn(name = "follower_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "followee_id", referencedColumnName = "id"))
    @OneToMany(cascade = REMOVE)
    private Set<User> followingUsers = new HashSet<>();

    @Transient
    private boolean anonymous = false;

    public User(Email email, Password password, Profile profile) {
        this(null, email, password, profile, null, false);
    }

    private User(Long id, Email email, Password password, Profile profile, String token, boolean anonymous) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.profile = profile;
        this.token = token;
        this.anonymous = anonymous;
    }

    public static User anonymous() {
        return new User.Builder().id(null).anonymous(true).build();
    }

    public Long getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public Profile getProfile() {
        return profile;
    }

    public Password getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public boolean matchesPassword(String rawPassword, PasswordEncoder passwordEncoder) {
        return password.matchesPassword(rawPassword, passwordEncoder);
    }

    public User setToken(String token) {
        checkArgument(isNotEmpty(token) || isNotBlank(token), "token must not be null or blank!");
        this.token = token;
        return this;
    }

    public boolean isAnonymous() {
        return this. id == null && this.anonymous;
    }

    public void updateEmail(Email email) {
        this.email = email;
    }

    public void updateUsername(String username) {
        this.profile.updateUsername(username);
    }

    public void updateBio(String bio) {
        this.profile.updateBio(bio);
    }

    public void updatePassword(Password password) {
        this.password = password;
    }

    public void updateImage(String image) {
        this.profile.updateImage(image);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("name", profile.getUsername())
                .append("email", email.getAddress())
                .append("password", "[PROTECTED]")
                .toString();
    }

    static public class Builder {
        private Long id;
        private Email email;
        private Password password;
        private Profile profile;
        private String token;
        private boolean anonymous;

        public Builder() {
        }

        public Builder(User user) {
            this.id = user.id;
            this.email = user.email;
            this.password = user.password;
            this.profile = user.profile;
            this.token = user.token;
            this.anonymous = user.anonymous;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder email(Email email) {
            this.email = email;
            return this;
        }

        public Builder password(Password password) {
            this.password = password;
            return this;
        }

        public Builder profile(Profile profile) {
            this.profile = profile;
            return this;
        }

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder anonymous(boolean anonymous) {
            this.anonymous = anonymous;
            return this;
        }

        public User build() {
            return new User(id, email, password, profile, token, anonymous);
        }
    }
}

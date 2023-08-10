package com.io.realworldjpa.domain.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.GenerationType.IDENTITY;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

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

    @Column(name = "password")
    private String password;

    @Transient
    private String token;

    @Transient
    private boolean anonymous = false;

    @JoinTable(name = "users_followings",
            joinColumns = @JoinColumn(name = "follower_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "followee_id", referencedColumnName = "id"))
    @OneToMany(cascade = REMOVE)
    private Set<User> followingUsers = new HashSet<>();

    public User(Email email, String password, Profile profile) {
        this(null, email, password, profile, null, false);
    }

    private User(Long id, Email email, String password, Profile profile, String token, boolean anonymous) {
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

    public String getPassword() {
        return password;
    }

    public User setToken(String token) {
        checkArgument(isNotBlank(token), "token must not be null or blank!");
        this.token = token;
        return this;
    }

    public boolean isAnonymous() {
        return this. id == null && this.anonymous;
    }


    @Override
    public boolean equals(Object o) {
        return o instanceof User other && Objects.equals(this.id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    static public class Builder {
        private Long id;
        private Email email;
        private String password;
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

        public Builder password(String password) {
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

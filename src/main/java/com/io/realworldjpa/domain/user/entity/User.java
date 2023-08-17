package com.io.realworldjpa.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static jakarta.persistence.GenerationType.IDENTITY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Transient
    private String token;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "from", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Follow> following = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "to", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Follow> follower = new HashSet<>();

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

    public Set<Follow> getFollowing() {
        return following;
    }

    public Set<Follow> getFollower() {
        return follower;
    }

    public boolean matchesPassword(String rawPassword, PasswordEncoder passwordEncoder) {
        return password.matchesPassword(rawPassword, passwordEncoder);
    }

    public User setToken(String token) {
        checkArgument(isNotEmpty(token) || isNotBlank(token), "토큰이 존재하지 않습니다.");
        this.token = token;
        return this;
    }

    public boolean isAnonymous() {
        return this.id == null && this.anonymous;
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

    public void followUser(User targetUser) {
        if (targetUser == null || targetUser.isAnonymous()) {
            throw new IllegalArgumentException("팔로우 대상이 존재하지 않습니다.");
        }

        if (isAlreadyFollow(targetUser)) {
            return ;
        }

        Follow follow = new Follow(this, targetUser);
        this.following.add(follow);
        follow.getTo().getFollower().add(follow);
    }

    public boolean isAlreadyFollow(User to) {
        Follow follow = new Follow(this, to);
        return this.following.stream().anyMatch(follow::equals);
    }

    public void unfollowUser(User to) {
        if (to == null || to.isAnonymous()) {
            throw new IllegalArgumentException("팔로우 대상이 존재하지 않습니다.");
        }
        Optional<Follow> currentFollow = this.following.stream().filter(to::isFollowing).findFirst();
        currentFollow.ifPresent(follow -> {
            this.following.remove(follow);
            this.follower.remove(follow);
        });
    }

    private boolean isFollowing(Follow follow) {
        return follow.getTo().equals(this);
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

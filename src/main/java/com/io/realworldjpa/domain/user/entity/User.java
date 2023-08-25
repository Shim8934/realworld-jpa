package com.io.realworldjpa.domain.user.entity;

import com.io.realworldjpa.domain.article.entity.Article;
import com.io.realworldjpa.domain.article.entity.ArticleFavorite;
import com.io.realworldjpa.global.util.Generated;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static jakarta.persistence.GenerationType.IDENTITY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id")
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ArticleFavorite> favoriteArticles = new HashSet<>();

    @Transient
    private boolean anonymous = false;

    protected User() {}

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
        checkArgument(isNotBlank(token), "토큰이 존재하지 않습니다.");
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
        if (isAlreadyFollow(targetUser)) {
            throw new IllegalArgumentException("이미 팔로우한 유저입니다.");
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
        Optional<Follow> currentFollow = this.following.stream().filter(to::isFollowing).findFirst();
        currentFollow.ifPresent(follow -> {
            this.following.remove(follow);
            this.follower.remove(follow);
        });
    }

    private boolean isFollowing(Follow follow) {
        return follow.getTo().equals(this);
    }

    public void favoriteArticle(Article article) {
        if (article == null) {
            throw new IllegalArgumentException("게시글이 존재하지 않습니다.");
        }

        if (isAlreadyFavorite(article)) {
            throw new IllegalArgumentException("이미 팔로우한 게시글입니다.");
        }

        ArticleFavorite articleFavorite = new ArticleFavorite(this, article);
        addFavoriteArticleThisUser(articleFavorite);
        addThisUserToFavoriteArticle(articleFavorite);
    }

    private void addFavoriteArticleThisUser(ArticleFavorite articleFavorite) {
        this.favoriteArticles.add(articleFavorite);
    }

    private void addThisUserToFavoriteArticle(ArticleFavorite articleFavorite) {
        articleFavorite.getArticle().getFavoriteUsers().add(articleFavorite);
    }

    public boolean isAlreadyFavorite(Article article) {
        ArticleFavorite articleFavorite = new ArticleFavorite(this, article);
        return this.favoriteArticles.stream().anyMatch(articleFavorite::equals);
    }

    public void unfavoriteArticle(Article article) {
        if (article == null) {
            throw new IllegalArgumentException("게시글이 존재하지 않습니다.");
        }
        findFavoriteArticle(article).ifPresent(
                articleFavorite -> {
                    this.favoriteArticles.remove(article);
                    articleFavorite.getArticle().getFavoriteUsers().remove(articleFavorite);
                }
        );
    }

    private Optional<ArticleFavorite> findFavoriteArticle(Article article) {
        return this.favoriteArticles.stream().filter(article::equalsArticle).findFirst();
    }

    public List<User> followUserList() {
        return this.following.stream().map(Follow::getTo).toList();
    }

    @Override
    @Generated
    public boolean equals(Object o) {
        return o instanceof User user
                && Objects.equals(this.id, user.id);
    }

    @Override
    @Generated
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    @Generated
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("name", profile.getUsername())
                .append("email", email.getAddress())
                .append("password", "[PROTECTED]")
                .toString();
    }

    @Generated
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

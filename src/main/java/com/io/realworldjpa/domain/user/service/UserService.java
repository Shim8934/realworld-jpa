package com.io.realworldjpa.domain.user.service;

import com.io.realworldjpa.domain.user.entity.*;
import com.io.realworldjpa.domain.user.model.LoginRequest;
import com.io.realworldjpa.domain.user.model.UserPostRequest;
import com.io.realworldjpa.domain.user.model.UserPutRequest;
import com.io.realworldjpa.global.security.JwtCustomProvider;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.io.realworldjpa.domain.user.entity.Email.*;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtCustomProvider jwtCustomProvider;

    @Transactional
    public User signUp(UserPostRequest userPostRequest) {
        checkArgument(isNotEmpty(userPostRequest.password()), "password must not be null or blank!");

        if (userRepository.existsByEmail(of(userPostRequest.email()))) {
            throw new IllegalArgumentException("이미 존재하는 Email - ['%s'] 입니다.".formatted(userPostRequest.email()));
        };
        return userRepository.save(createNewUser(userPostRequest));
    }

    @Transactional(readOnly = true)
    public UserDto login(LoginRequest loginRequest) {
        checkArgument(isNotEmpty(loginRequest.password()), "password must not be null or blank!");

        return userRepository.findFirstByEmail(of(loginRequest.email()))
                .filter(user -> user.matchesPassword(loginRequest.password(), passwordEncoder))
                .map(user -> {
                    String token = null;
                    try {
                        token = jwtCustomProvider.jwtFromUser(user);
                    } catch (JOSEException e) {
                        throw new RuntimeException(e);
                    }
                    return new UserDto(user, token);
                })
                .orElseThrow(() -> new IllegalArgumentException("적절하지 않은 Email - ['%s'] 입니다.".formatted(loginRequest.email())));
    }

    @Transactional
    public UserDto updateUser(User updateUser, UserPutRequest putRequest) {
        checkArgument(isNotEmpty(putRequest.email()), "email must not be null or blank!");

        Email putEmail = Email.of(putRequest.email());
        if (!updateUser.getEmail().equals(putEmail) && userRepository.existsByEmail(putEmail)) {
            throw new IllegalArgumentException("이미 존재하는 이메일 - ['%s'] 입니다.".formatted(putRequest.email()));
        }
        else {
            updateUser.updateEmail(putEmail);
        }

        updateUser.updateUsername(defaultIfNull(putRequest.username(), updateUser.getProfile().getUsername()));
        updateUser.updateBio(defaultIfNull(putRequest.bio(), updateUser.getProfile().getBio()));
        if (putRequest.password() != null) {
            updateUser.updatePassword(Password.of(putRequest.password(), passwordEncoder));
        }
        updateUser.updateImage(defaultIfNull(putRequest.image(), updateUser.getProfile().getImage()));

        return new UserDto(updateUser, updateUser.getToken());
    }

    private User createNewUser(UserPostRequest userPostRequest) {
        return new User.Builder()
                .email(Email.of(userPostRequest.email()))
                .profile(Profile.of(userPostRequest.username(), userPostRequest.bio(), userPostRequest.image()))
                .password(Password.of(userPostRequest.password(), passwordEncoder))
                .build();
    }

}

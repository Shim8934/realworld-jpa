package com.io.realworldjpa.domain.user.service;

import com.io.realworldjpa.domain.user.entity.Email;
import com.io.realworldjpa.domain.user.entity.Password;
import com.io.realworldjpa.domain.user.entity.Profile;
import com.io.realworldjpa.domain.user.entity.User;
import com.io.realworldjpa.domain.user.model.LoginRequest;
import com.io.realworldjpa.domain.user.model.UserPostRequest;
import com.io.realworldjpa.domain.user.model.UserPutRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User signUp(UserPostRequest userPostRequest) {
        checkArgument(isNotEmpty(userPostRequest.password()), "password must not be null or blank!");
        if (userRepository.existsByEmail(new Email(userPostRequest.email()))) {
            throw new IllegalArgumentException("이미 존재하는 Email - ['%s'] 입니다.".formatted(userPostRequest.email()));
        };
        return userRepository.save( createNewUser(userPostRequest) );
    }
    @Transactional(readOnly = true)
    public User login(LoginRequest loginRequest) {
        checkArgument(isNotEmpty(loginRequest.password()), "password must not be null or blank!");
        return userRepository.findFirstByEmail(new Email(loginRequest.email()))
                .filter(user -> user.matchesPassword(loginRequest.password(), passwordEncoder))
                .orElseThrow(() -> new IllegalArgumentException("적절하지 않은 Email - ['%s'] 입니다.".formatted(loginRequest.email())));
    }

    @Transactional
    public User updateUser(User user, UserPutRequest putRequest) {
        checkArgument(isNotEmpty(putRequest.email()) || isNotBlank(putRequest.email()), "email must not be null or blank!");
        checkArgument(isNotEmpty(putRequest.password()), "password must be provided.");

        Email putEmail = new Email(putRequest.email());
        if (!user.getEmail().equals(putEmail) && userRepository.existsByEmail(putEmail)) {
            throw new IllegalArgumentException("이미 존재하는 이메일 - ['%s'] 입니다.".formatted(putRequest.email()));
        }
        else {
            user.updateEmail(putEmail);
        }

        user.updateUsername(putRequest.username());
        user.updateBio(putRequest.bio());
        user.updatePassword(Password.of(putRequest.password(), passwordEncoder));
        user.updateImage(putRequest.image());

        return user;
    }

    private User createNewUser(UserPostRequest userPostRequest) {
        return new User.Builder()
                .email(new Email(userPostRequest.email()))
                .profile(new Profile(userPostRequest.username()))
                .password(Password.of(userPostRequest.password(), passwordEncoder))
                .build();
    }

}

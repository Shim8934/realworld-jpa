package com.io.realworldjpa.domain.user.service;

import com.io.realworldjpa.domain.user.entity.Email;
import com.io.realworldjpa.domain.user.entity.Profile;
import com.io.realworldjpa.domain.user.entity.User;
import com.io.realworldjpa.domain.user.model.LoginRequest;
import com.io.realworldjpa.domain.user.model.UserPostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User signUp(UserPostRequest userPostRequest) {
        if (userRepository.existsByEmail(new Email(userPostRequest.email()))) {
            throw new IllegalArgumentException("이미 존재하는 Email(%s) 입니다.".formatted(userPostRequest.email()));
        };
        return userRepository.save( createNewUser(userPostRequest) );
    }
    @Transactional(readOnly = true)
    public User login(LoginRequest loginRequest) {
        return userRepository.findFirstByEmail(new Email(loginRequest.email()))
                .filter(user -> passwordEncoder.matches(loginRequest.password(), user.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("적절하지 않은 Email(%s)입니다.".formatted(loginRequest.email())));
    }

    @Transactional(readOnly = true)
    public User findById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("적절하지 않은 요청입니다."));
    }

    private User createNewUser(UserPostRequest userPostRequest) {
        return new User.Builder()
                .email(new Email(userPostRequest.email()))
                .profile(new Profile(userPostRequest.username()))
                .password(passwordEncoder.encode(userPostRequest.password()))
                .build();
    }
}

package com.io.realworldjpa.domain.user.service;

import com.io.realworldjpa.domain.user.entity.ProfileDto;
import com.io.realworldjpa.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public ProfileDto getProfile(User viewer, String targetName) {
        User targetUser = findTargetUserByUsername(targetName);

        return new ProfileDto(viewer, targetUser);
    }

    @Transactional
    public ProfileDto follow(User from, String target) {
        User targetUser = findTargetUserByUsername(target);
        from.followUser(targetUser);
        return new ProfileDto(from, targetUser);
    }

    @Transactional
    public ProfileDto unfollow(User from, String target) {
        User targetUser = findTargetUserByUsername(target);
        from.unfollowUser(targetUser);
        return new ProfileDto(from, targetUser);
    }

    private User findTargetUserByUsername(String targetName) {
        return userRepository.findByProfileUsername(targetName)
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없는 User - ['%s'] 입니다.".formatted(targetName)));
    }
}

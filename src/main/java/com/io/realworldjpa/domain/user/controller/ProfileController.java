package com.io.realworldjpa.domain.user.controller;

import com.io.realworldjpa.domain.user.entity.User;
import com.io.realworldjpa.domain.user.model.ProfileRecord;
import com.io.realworldjpa.domain.user.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/{username}")
    public ProfileRecord getUserProfile(User viewer, @PathVariable("username") String target) {
        return new ProfileRecord(profileService.getProfile(viewer, target));
    }

    @PostMapping("/{username}/follow")
    public ProfileRecord followUser(User from, @PathVariable("username") String target) {
        return new ProfileRecord(profileService.follow(from, target));
    }

    @DeleteMapping("/{username}/follow")
    public ProfileRecord unfollowUser(User from, @PathVariable("username") String target) {
        return new ProfileRecord(profileService.unfollow(from, target));
    }

}

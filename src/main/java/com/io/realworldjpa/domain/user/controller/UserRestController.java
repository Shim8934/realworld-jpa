package com.io.realworldjpa.domain.user.controller;

import com.io.realworldjpa.domain.security.UserJwtPayload;
import com.io.realworldjpa.domain.security.service.JwtEncoder;
import com.io.realworldjpa.domain.user.entity.User;
import com.io.realworldjpa.domain.user.model.LoginRequest;
import com.io.realworldjpa.domain.user.model.UserPostRequest;
import com.io.realworldjpa.domain.user.model.UserRecord;
import com.io.realworldjpa.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserRestController {

    private final UserService userService;

    private final JwtEncoder jwtEncoder;

    @PostMapping("/users")
    public ModelAndView registUser(@RequestBody UserPostRequest userPostRequest, HttpServletRequest httpServletRequest) {
        User postUser = userService.signUp(userPostRequest);

        LoginRequest loginRequest = new LoginRequest(userPostRequest.email(), userPostRequest.password());
        httpServletRequest.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
        return new ModelAndView("redirect:/api/users/login", "user", Map.of("user", loginRequest));
    }

    @ResponseStatus(CREATED)
    @PostMapping("/users/login")
    public UserRecord loginUser(@RequestBody LoginRequest loginRequest) {
        User loginUser = userService.login(loginRequest);
        return new UserRecord(loginUser, jwtEncoder.newJwtFromUser(loginUser));
    }

    @GetMapping("/user")
    public void getUser(@AuthenticationPrincipal UserJwtPayload userJwtPayload) {
        System.out.println("jwt = " + userJwtPayload.toString());
    }

    private static String getCredential() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getCredentials()
                .toString();
    }
}

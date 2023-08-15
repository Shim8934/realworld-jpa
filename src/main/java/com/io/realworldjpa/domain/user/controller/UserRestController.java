package com.io.realworldjpa.domain.user.controller;

import com.io.realworldjpa.domain.user.entity.User;
import com.io.realworldjpa.domain.user.model.LoginRequest;
import com.io.realworldjpa.domain.user.model.UserPostRequest;
import com.io.realworldjpa.domain.user.model.UserPutRequest;
import com.io.realworldjpa.domain.user.model.UserRecord;
import com.io.realworldjpa.domain.user.service.UserService;
import com.io.realworldjpa.global.security.JwtCustomProvider;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private final JwtCustomProvider jwtCustomProvider;


    @PostMapping("/users")
    public ModelAndView registUser(@RequestBody UserPostRequest userPostRequest, HttpServletRequest httpServletRequest) {
        userService.signUp(userPostRequest);

        LoginRequest loginRequest = new LoginRequest(userPostRequest.email(), userPostRequest.password());
        httpServletRequest.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
        return new ModelAndView("redirect:/api/users/login", "user", Map.of("user", loginRequest));
    }

    @ResponseStatus(CREATED)
    @PostMapping("/users/login")
    public UserRecord loginUser(@RequestBody LoginRequest loginRequest) throws JOSEException {
        User loginUser = userService.login(loginRequest);

        return new UserRecord(loginUser, jwtCustomProvider.jwtFromUser(loginUser));
    }

    @GetMapping("/user")
    public UserRecord getUser(User getUser) {
        return new UserRecord(getUser, getUser.getToken());
    }

    @PutMapping("/user")
    public UserRecord putUser(User updateUser, @RequestBody UserPutRequest putRequest) {
        User putUser = userService.updateUser(updateUser, putRequest);
        return new UserRecord(putUser, putUser.getToken());
    }
}

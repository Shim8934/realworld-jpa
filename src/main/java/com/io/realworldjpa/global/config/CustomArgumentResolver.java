package com.io.realworldjpa.global.config;

import com.io.realworldjpa.domain.user.entity.User;
import com.io.realworldjpa.domain.user.service.UserRepository;
import com.io.realworldjpa.global.util.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class CustomArgumentResolver implements HandlerMethodArgumentResolver {
    private final UserRepository userRepository;

    @Override
    @Generated
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == User.class;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (authentication instanceof AnonymousAuthenticationToken) {
            return User.anonymous();
        }

        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        long userId = Long.parseLong(jwtAuthenticationToken.getName());
        String token = jwtAuthenticationToken.getToken().getTokenValue().strip();

        return userRepository
                .findById(userId)
                .map(user -> user.setToken(token))
                .orElseThrow(() -> new InvalidBearerTokenException("유효하지 않은 토큰입니다. [`%s`] ".formatted(token)));
    }
}

package com.io.realworldjpa.global.security;

import com.io.realworldjpa.global.config.ExceptionHandleInterceptor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtHandleFilter extends OncePerRequestFilter {
    private final ExceptionHandleInterceptor delegator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            delegator.handle(e);
        }
    }
}

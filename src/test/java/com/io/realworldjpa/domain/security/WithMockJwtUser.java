package com.io.realworldjpa.domain.security;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@WithSecurityContext(factory = WithMockJwtUserContextFactory.class)
public @interface WithMockJwtUser {

    long userId() default 1L;

    String name() default "testUsername";

    String email() default "testEmail@gmail.com";

    String role() default "USER";

}

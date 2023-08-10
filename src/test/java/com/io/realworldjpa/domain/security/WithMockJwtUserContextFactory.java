package com.io.realworldjpa.domain.security;

import com.io.realworldjpa.domain.security.service.JwtPayload;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import static java.util.Collections.singleton;
import static org.mockito.Mockito.mock;

public class WithMockJwtUserContextFactory implements WithSecurityContextFactory<WithMockJwtUser> {

  @Override
  public SecurityContext createSecurityContext(WithMockJwtUser annotation) {
    final var context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(new MockJWTAuthentication(mock(JwtPayload.class)));
    return context;
  }

  private static class MockJWTAuthentication extends AbstractAuthenticationToken {

    private final JwtPayload jwtPayload;

    private MockJWTAuthentication(JwtPayload jwtPayload) {
      super(singleton(new SimpleGrantedAuthority("USER")));
      super.setAuthenticated(true);
      this.jwtPayload = jwtPayload;
    }

    @Override
    public Object getPrincipal() {
      return jwtPayload;
    }

    @Override
    public Object getCredentials() {
      return "MOCKED CREDENTIAL";
    }
  }
}
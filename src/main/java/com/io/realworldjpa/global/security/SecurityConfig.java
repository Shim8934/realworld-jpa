package com.io.realworldjpa.global.security;

import com.io.realworldjpa.global.config.ExceptionHandleFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtConfig jwtConfig;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, ExceptionHandleFilter exceptionHandleFilter) throws Exception {
        http.httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .cors(SecurityConfigurerAdapter::and)

                .authorizeHttpRequests(
                        requests -> requests.requestMatchers(HttpMethod.POST, "/api/users", "/api/users/login")
                                .permitAll()
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/articles/{slug}/comments",
                                        "/api/articles/{slug}",
                                        "/api/articles",
                                        "/api/profiles/{username}",
                                        "/api/tags")
                                .permitAll()
                                .anyRequest()
                                .authenticated())

                .addFilterBefore(exceptionHandleFilter, UsernamePasswordAuthenticationFilter.class)

                .oauth2ResourceServer()
                .jwt();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration();

        cors.setAllowedOriginPatterns(List.of("*"));
        cors.setAllowedMethods(List.of("*"));
        cors.setAllowedHeaders(List.of("*"));
        cors.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);

        return source;
    }

    @Bean
    public JwtCustomProvider jwtProvider() {
        return new JwtCustomProvider(getSHA512Hash(jwtConfig.getSecretKey()), jwtConfig.getIssuer(), jwtConfig.getExpMinute());
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        MacAlgorithm algorithm = MacAlgorithm.HS512;
        return NimbusJwtDecoder.withSecretKey(new SecretKeySpec(getSHA512Hash(jwtConfig.getSecretKey()), algorithm.getName()))
                .macAlgorithm(algorithm)
                .build();
    }

    private static byte[] getSHA512Hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            return digest.digest(input.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Cannot find SHA-512 algorithm", e);
        }
    }
}
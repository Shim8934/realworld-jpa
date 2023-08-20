package com.io.realworldjpa.domain.user.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("Email Entity Test")
class EmailTest {

    private static final String NAME = "shimki";
    private static final String DOMAIN = "example.com";
    private static final String EMAIL = "shimki@example.com";

    @Test
    @DisplayName("Generate_Regular_Email")
    void generate_regular_email() {
        Email testEmail = new Email("shimki@example.com");

        assertThat(testEmail.getName()).isEqualTo(NAME);
        assertThat(testEmail.getDomain()).isEqualTo(DOMAIN);
    }

    @Test
    @DisplayName("Wrong_Email_Expect_Error")
    void generate_wrong_email_expect_error() {
        assertThatThrownBy(() -> new Email(NAME + DOMAIN))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Wrong_length_Expect_Error")
    void Wrong_length_expect_error() {
        assertThatThrownBy(() -> new Email("ly@exam"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Email("aaaaaaaaaa" +
                                                    "aaaaaaaaaa" +
                                                    "aaaaaaaaaa" +
                                                    "aaaaaaaaaa" +
                                                    "@examample.com"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Equals Test")
    void equal_test() {
        Email testEmail = new Email("shimki@example.com");
        Email testEmail2 = new Email(EMAIL);
        Email notEqualEmail = new Email("shimki@test.com");

        assertThat(testEmail.getName()).isEqualTo(NAME);
        assertThat(testEmail.getDomain()).isEqualTo(DOMAIN);
        assertThat(testEmail).isEqualTo(testEmail2);
        assertThat(testEmail).isNotEqualTo(notEqualEmail);
    }
}
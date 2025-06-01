package com.system.design.urlshortener.domain.entity;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UrlValidationTest {
    @Test
    void shouldThrowIfIdIsNull() {
        assertThatThrownBy(() -> new Url(null, "abc123", "https://example.com", OffsetDateTime.now()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("id must not be null");
    }

    @Test
    void shouldThrowIfShortUrlIsNull() {
        assertThatThrownBy(() -> new Url(UUID.randomUUID(), null, "https://example.com", OffsetDateTime.now()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("shortUrl must not be null");
    }

    @Test
    void shouldThrowIfShortUrlIsBlank() {
        assertThatThrownBy(() -> new Url(UUID.randomUUID(), "   ", "https://example.com", OffsetDateTime.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("shortUrl must not be blank");
    }

    @Test
    void shouldThrowIfShortUrlIsNotAlphanumericOrTooShort() {
        assertThatThrownBy(() -> new Url(UUID.randomUUID(), "abc!@#", "https://example.com", OffsetDateTime.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("shortUrl must be alphanumeric and at least 6 characters");
        assertThatThrownBy(() -> new Url(UUID.randomUUID(), "abc1", "https://example.com", OffsetDateTime.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("shortUrl must be alphanumeric and at least 6 characters");
    }

    @Test
    void shouldThrowIfOriginalUrlIsNull() {
        assertThatThrownBy(() -> new Url(UUID.randomUUID(), "abc123", null, OffsetDateTime.now()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("originalUrl must not be null");
    }

    @Test
    void shouldThrowIfOriginalUrlIsBlank() {
        assertThatThrownBy(() -> new Url(UUID.randomUUID(), "abc123", "   ", OffsetDateTime.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("originalUrl must not be blank");
    }

    @Test
    void shouldThrowIfOriginalUrlIsNotHttpOrHttps() {
        assertThatThrownBy(() -> new Url(UUID.randomUUID(), "abc123", "ftp://example.com", OffsetDateTime.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("originalUrl must be a valid http or https URL");
        assertThatThrownBy(() -> new Url(UUID.randomUUID(), "abc123", "example.com", OffsetDateTime.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("originalUrl must be a valid http or https URL");
    }
}


package com.system.design.urlshortener.domain.entity;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Represents a shortened URL mapping in the system.
 *
 * @param id          Unique identifier for the URL mapping
 * @param shortUrl    The generated short code for the original URL
 * @param originalUrl The original long URL
 * @param createdAt   The timestamp when the mapping was created
 */
public record Url(
        UUID id,
        String shortUrl,
        String originalUrl,
        OffsetDateTime createdAt
) {
    // shortUrl must be alphanumeric and at least 6 characters (regex: ^[0-9a-zA-Z]{6,}$)
    private static final Pattern SHORT_URL_PATTERN = Pattern.compile("^[0-9a-zA-Z]{6,}$");

    private static final Pattern URL_PATTERN = Pattern.compile("^(https?://).+");

    public Url {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(shortUrl, "shortUrl must not be null");
        Objects.requireNonNull(originalUrl, "originalUrl must not be null");

        if (shortUrl.isBlank()) {
            throw new IllegalArgumentException("shortUrl must not be blank");
        }

        if (!SHORT_URL_PATTERN.matcher(shortUrl).matches()) {
            throw new IllegalArgumentException("shortUrl must be alphanumeric and at least 6 characters");
        }

        if (originalUrl.isBlank()) {
            throw new IllegalArgumentException("originalUrl must not be blank");
        }
        if (!URL_PATTERN.matcher(originalUrl).matches()) {
            throw new IllegalArgumentException("originalUrl must be a valid http or https URL");
        }
    }
}

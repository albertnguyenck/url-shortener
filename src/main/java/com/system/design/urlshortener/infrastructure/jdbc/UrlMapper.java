package com.system.design.urlshortener.infrastructure.jdbc;

import com.system.design.urlshortener.domain.entity.Url;

import java.util.Objects;

public class UrlMapper {
    public static UrlEntity toEntity(Url domain) {
        Objects.requireNonNull(domain);
        return new UrlEntity(
            domain.id(),
            domain.shortUrl(),
            domain.originalUrl(),
            domain.createdAt()
        );
    }

    public static Url toDomain(UrlEntity entity) {
        Objects.requireNonNull(entity);
        return new Url(
            entity.getId(),
            entity.getShortUrl(),
            entity.getOriginalUrl(),
            entity.getCreatedAt()
        );
    }
}


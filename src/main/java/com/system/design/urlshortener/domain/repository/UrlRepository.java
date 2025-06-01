package com.system.design.urlshortener.domain.repository;

import com.system.design.urlshortener.domain.entity.Url;
import java.util.Optional;

public interface UrlRepository {
    Optional<Url> findByShortUrl(String shortUrl);

    Optional<Url> findByLongUrl(String longUrl);

    Url save(Url url);
}

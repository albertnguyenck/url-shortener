package com.system.design.urlshortener.application.service;

import com.system.design.urlshortener.domain.entity.Url;
import com.system.design.urlshortener.domain.repository.UrlRepository;
import com.system.design.urlshortener.domain.util.ShortUrlGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UrlShortenerService {
    private static final Logger logger = LoggerFactory.getLogger(UrlShortenerService.class);
    private final UrlRepository urlRepository;

    public UrlShortenerService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public Url shortenUrl(String originalUrl) {
        logger.info("Shortening URL: {}", originalUrl);
        Optional<Url> existing = urlRepository.findByLongUrl(originalUrl);
        if (existing.isPresent()) {
            logger.info("Found existing short URL for original URL: {} -> {}", originalUrl, existing.get().shortUrl());
            return existing.get();
        }

        String shortUrl = ShortUrlGenerator.generate(originalUrl);
        int attempt = 0;
        String candidate = shortUrl;
        while (urlRepository.findByShortUrl(candidate).isPresent()) {
            logger.warn("Collision detected for shortUrl '{}', generating a new code (attempt {}).", candidate, attempt + 1);
            candidate = ShortUrlGenerator.generate(originalUrl + ":" + (++attempt));
        }
        logger.info("Generated new short URL: {} for original URL: {}", candidate, originalUrl);
        Url url = new Url(
            UUID.randomUUID(),
            candidate,
            originalUrl,
            OffsetDateTime.now()
        );
        return urlRepository.save(url);
    }

    public Optional<Url> getOriginalUrl(String shortUrl) {
        return urlRepository.findByShortUrl(shortUrl);
    }
}


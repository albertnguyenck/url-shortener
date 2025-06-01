package com.system.design.urlshortener.application.service;

import com.system.design.urlshortener.domain.entity.Url;
import com.system.design.urlshortener.domain.repository.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UrlShortenerServiceTest {
    private UrlRepository urlRepository;
    private UrlShortenerService urlShortenerService;

    @BeforeEach
    public void setUp() {
        urlRepository = Mockito.mock(UrlRepository.class);
        urlShortenerService = new UrlShortenerService(urlRepository);
    }

    @Test
    public void shortenUrl_shouldReturnShortenedUrl() {
        String originalUrl = "https://example.com";
        when(urlRepository.findByShortUrl(any())).thenReturn(Optional.empty());
        when(urlRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Url result = urlShortenerService.shortenUrl(originalUrl);

        assertThat(result.originalUrl()).isEqualTo(originalUrl);
        assertThat(result.shortUrl()).isNotBlank();
        assertThat(result.createdAt()).isNotNull();
    }

    @Test
    public void getOriginalUrl_shouldReturnUrlIfExists() {
        String shortUrl = "abc123";
        Url url = new Url(UUID.randomUUID(), shortUrl, "https://example.com", OffsetDateTime.now());
        when(urlRepository.findByShortUrl(shortUrl)).thenReturn(Optional.of(url));

        Optional<Url> result = urlShortenerService.getOriginalUrl(shortUrl);
        assertThat(result).isPresent();
        assertThat(result.get().originalUrl()).isEqualTo("https://example.com");
    }

    @Test
    public void getOriginalUrl_shouldReturnEmptyIfNotFound() {
        when(urlRepository.findByShortUrl(any())).thenReturn(Optional.empty());
        Optional<Url> result = urlShortenerService.getOriginalUrl("notfound");
        assertThat(result).isEmpty();
    }

    @Test
    public void shortenUrl_shouldHandleCollision() {
        String originalUrl = "https://collision.com";
        when(urlRepository.findByShortUrl(any()))
            .thenReturn(Optional.of(new Url(UUID.randomUUID(), "random", originalUrl, OffsetDateTime.now())))
            .thenReturn(Optional.empty());
        when(urlRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Url result = urlShortenerService.shortenUrl(originalUrl);
        assertThat(result.shortUrl()).isNotBlank();
        assertThat(result.shortUrl()).isNotEqualTo("random");
    }

    @Test
    public void shortenUrl_shouldReturnExistingShortUrlIfOriginalExists() {
        String originalUrl = "https://existing.com";
        Url existingUrl = new Url(UUID.randomUUID(), "exist123", originalUrl, OffsetDateTime.now());
        when(urlRepository.findByLongUrl(originalUrl)).thenReturn(Optional.of(existingUrl));

        Url result = urlShortenerService.shortenUrl(originalUrl);
        assertThat(result.shortUrl()).isEqualTo("exist123");
        assertThat(result.originalUrl()).isEqualTo(originalUrl);
    }
}

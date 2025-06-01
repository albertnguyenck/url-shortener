package com.system.design.urlshortener.infrastructure.jdbc;

import com.system.design.urlshortener.domain.entity.Url;
import com.system.design.urlshortener.domain.repository.UrlRepository;
import org.springframework.stereotype.Component;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;

import java.util.Optional;

@Component
public class UrlRepositoryImpl implements UrlRepository {
    private final JdbcShortURLRepository jdbcRepo;

    public UrlRepositoryImpl(JdbcShortURLRepository jdbcRepo) {
        this.jdbcRepo = jdbcRepo;
    }

    @Override
    @Cacheable(value = "shortUrlCache", key = "#shortUrl", unless = "#result == null")
    public Optional<Url> findByShortUrl(String shortUrl) {
        return jdbcRepo.findByShortUrl(shortUrl).map(UrlMapper::toDomain);
    }

    @Override
    public Optional<Url> findByLongUrl(String longUrl) {
        return jdbcRepo.findByOriginalUrl(longUrl).map(UrlMapper::toDomain);
    }

    @Override
    // @CachePut(value = "shortUrlCache", key = "#url.shortUrl")
    public Url save(Url url) {
        return UrlMapper.toDomain(jdbcRepo.save(UrlMapper.toEntity(url)));
    }
}


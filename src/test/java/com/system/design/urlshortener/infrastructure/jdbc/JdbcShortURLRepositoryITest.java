package com.system.design.urlshortener.infrastructure.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Testcontainers
public class JdbcShortURLRepositoryITest {
    @Autowired
    private JdbcShortURLRepository jdbcShortURLRepository;

    // SuppressWarnings("resource") is used here because Testcontainers manages the lifecycle of the container automatically
    // when using the @Container annotation with JUnit Jupiter. No manual resource management is needed.
    @SuppressWarnings("resource")
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test-db")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.test.database.replace", () -> "none");
    }

    @Test
    void saveAndFindByShortUrl_shouldWork() {
        UUID id = UUID.randomUUID();
        String shortUrl = "abc123";
        String originalUrl = "https://example.com";
        OffsetDateTime createdAt = OffsetDateTime.now();
        UrlEntity entity = new UrlEntity(id, shortUrl, originalUrl, createdAt);
        jdbcShortURLRepository.save(entity);

        Optional<UrlEntity> found = jdbcShortURLRepository.findByShortUrl(shortUrl);
        assertThat(found).isPresent();
        assertThat(found.get().getShortUrl()).isEqualTo(shortUrl);
        assertThat(found.get().getOriginalUrl()).isEqualTo(originalUrl);
        assertThat(found.get().getId()).isEqualTo(id);
        assertThat(found.get().getCreatedAt()).isNotNull();
    }

    @Test
    void findByShortUrl_shouldReturnEmptyIfNotFound() {
        Optional<UrlEntity> found = jdbcShortURLRepository.findByShortUrl("notfound");
        assertThat(found).isEmpty();
    }
}


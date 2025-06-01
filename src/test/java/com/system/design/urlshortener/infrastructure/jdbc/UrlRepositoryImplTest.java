package com.system.design.urlshortener.infrastructure.jdbc;

import com.system.design.urlshortener.domain.entity.Url;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UrlRepositoryImplTest {
    private JdbcShortURLRepository jdbcRepo;
    private UrlRepositoryImpl urlRepositoryImpl;

    @BeforeEach
    void setUp() {
        jdbcRepo = Mockito.mock(JdbcShortURLRepository.class);
        urlRepositoryImpl = new UrlRepositoryImpl(jdbcRepo);
    }

    @Test
    void findByShortUrl_shouldReturnDomainUrlIfFound() {
        UrlEntity entity = new UrlEntity(UUID.randomUUID(), "short1", "https://foo.com", OffsetDateTime.now());
        when(jdbcRepo.findByShortUrl("short1")).thenReturn(Optional.of(entity));
        Optional<Url> result = urlRepositoryImpl.findByShortUrl("short1");
        assertThat(result).isPresent();
        assertThat(result.get().shortUrl()).isEqualTo("short1");
        assertThat(result.get().originalUrl()).isEqualTo("https://foo.com");
    }

    @Test
    void findByShortUrl_shouldReturnEmptyIfNotFound() {
        when(jdbcRepo.findByShortUrl("notfound")).thenReturn(Optional.empty());
        Optional<Url> result = urlRepositoryImpl.findByShortUrl("notfound");
        assertThat(result).isEmpty();
    }

    @Test
    void save_shouldCallRepoAndReturnDomainUrl() {
        Url url = new Url(UUID.randomUUID(), "short2", "https://bar.com", OffsetDateTime.now());
        UrlEntity entity = UrlMapper.toEntity(url);
        when(jdbcRepo.save(any())).thenReturn(entity);
        Url saved = urlRepositoryImpl.save(url);
        assertThat(saved.shortUrl()).isEqualTo("short2");
        assertThat(saved.originalUrl()).isEqualTo("https://bar.com");
    }

    @Test
    void save_shouldDelegateToRepo() {
        Url url = new Url(UUID.randomUUID(), "short3", "https://baz.com", OffsetDateTime.now());
        UrlEntity entity = UrlMapper.toEntity(url);
        when(jdbcRepo.save(any())).thenReturn(entity);
        urlRepositoryImpl.save(url);
        verify(jdbcRepo, times(1)).save(any());
    }
}


package com.system.design.urlshortener.presentation.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.system.design.urlshortener.application.dto.CreateShortUrlRequest;
import com.system.design.urlshortener.application.service.UrlShortenerService;
import com.system.design.urlshortener.domain.entity.Url;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {UrlShortenerController.class, RedirectController.class})
public class UrlShortenerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private UrlShortenerService urlShortenerService;

    private Url url;

    @BeforeEach
    public void setUp() {
        url = new Url(UUID.randomUUID(), "abc123", "https://example.com", java.time.OffsetDateTime.now());
    }

    @Test
    public void shortenUrl_shouldReturnShortenedUrl() throws Exception {
        when(urlShortenerService.shortenUrl(any())).thenReturn(url);
        CreateShortUrlRequest request = new CreateShortUrlRequest("https://example.com");
        mockMvc.perform(post("/api/urls/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.originalUrl").value("https://example.com"))
                .andExpect(jsonPath("$.shortUrl").value("abc123"));
    }

    @Test
    public void redirectToOriginalUrl_shouldRedirectIfFound() throws Exception {
        when(urlShortenerService.getOriginalUrl("abc123")).thenReturn(Optional.of(url));
        mockMvc.perform(get("/abc123"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "https://example.com"));
    }

    @Test
    public void redirectToOriginalUrl_shouldReturnNotFoundIfNotExists() throws Exception {
        when(urlShortenerService.getOriginalUrl("notfound")).thenReturn(Optional.empty());
        mockMvc.perform(get("/notfound"))
                .andExpect(status().isNotFound());
    }
}

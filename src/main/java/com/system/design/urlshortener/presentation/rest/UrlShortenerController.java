package com.system.design.urlshortener.presentation.rest;

import com.system.design.urlshortener.application.dto.CreateShortUrlRequest;
import com.system.design.urlshortener.application.service.UrlShortenerService;
import com.system.design.urlshortener.domain.entity.Url;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/urls")
public class UrlShortenerController {
    private final UrlShortenerService urlShortenerService;

    public UrlShortenerController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<Url> shortenUrl(@RequestBody CreateShortUrlRequest request) {
        Url url = urlShortenerService.shortenUrl(request.originalUrl());
        return ResponseEntity.ok(url);
    }
}


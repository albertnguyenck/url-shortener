package com.system.design.urlshortener.presentation.rest;

import com.system.design.urlshortener.application.service.UrlShortenerService;
import com.system.design.urlshortener.domain.entity.Url;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
class RedirectController {
    private final UrlShortenerService urlShortenerService;

    public RedirectController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortUrl) {
        Optional<Url> urlOptional = urlShortenerService.getOriginalUrl(shortUrl);
        if (urlOptional.isPresent()) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", urlOptional.get().originalUrl());
            return ResponseEntity.status(302).headers(headers).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

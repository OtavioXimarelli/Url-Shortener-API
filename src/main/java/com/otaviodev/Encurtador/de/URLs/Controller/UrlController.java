package com.otaviodev.Encurtador.de.URLs.Controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.otaviodev.Encurtador.de.URLs.Service.UrlRedirectService;
import com.otaviodev.Encurtador.de.URLs.Service.UrlShortnerService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/urls")
public class UrlController {
    private final UrlRedirectService urlRedirectService;
    private final UrlShortnerService urlShortnerService;


    public UrlController(UrlRedirectService urlRedirectService, UrlShortnerService urlShortnerService) {
        this.urlRedirectService = urlRedirectService;
        this.urlShortnerService = urlShortnerService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<String> shortenUrl(@RequestBody String url) throws JsonProcessingException {
        String shortCode = urlShortnerService.shortenUrl(url);
        return ResponseEntity.ok(shortCode);
    }

    @GetMapping("/{key}")
    public void redirectToUrl(@PathVariable String key, HttpServletResponse response) throws IOException {
        try {
            String originalUrl = urlRedirectService.getUrl(key, response);
            response.sendRedirect(originalUrl);
        } catch (RuntimeException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "URL not found");
        }


    }
}

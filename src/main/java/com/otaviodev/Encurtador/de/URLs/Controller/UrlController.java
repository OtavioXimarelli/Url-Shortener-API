package com.otaviodev.Encurtador.de.URLs.Controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.otaviodev.Encurtador.de.URLs.Service.UrlRedirectService;
import com.otaviodev.Encurtador.de.URLs.Service.UrlShortnerService;
import com.otaviodev.Encurtador.de.URLs.model.UrlModel;
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
    public ResponseEntity<String> shortenUrl(@RequestBody UrlModel url) throws JsonProcessingException {
        String shortCode = urlShortnerService.shortenUrl(url.getUrl());
        return ResponseEntity.ok(shortCode);
    }

    @GetMapping("/{key}")
    public void redirectToUrl(@PathVariable String key, HttpServletResponse response) {
        try {
            String originalUrl = urlRedirectService.getUrl(key);
            response.sendRedirect(originalUrl);
        } catch (Exception e) {
            try {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "URL não encontrada");
            } catch (IOException ex) {
                throw new RuntimeException("Erro ao processar redirecionamento", ex);
            }
        }
    }
}

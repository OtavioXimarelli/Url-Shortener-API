package com.otaviodev.Encurtador.de.URLs.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UrlRedirectService {
    private final S3Client s3Client;
    private final String bucketName = "otavio-urlshortner-bucket";
    private static final Logger logger = LoggerFactory.getLogger(UrlRedirectService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public UrlRedirectService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String getUrl(String key) {
        try {

            String jsonContent = getJsonFromS3(key);


            Map<String, String> urlData = objectMapper.readValue(jsonContent, new TypeReference<>() {
            });


            String originalUrl = urlData.get("url");

            if (originalUrl == null) {
                throw new IllegalStateException("URL não encontrada no JSON");
            }

            return originalUrl;
        } catch (Exception e) {
            logger.error("Erro ao recuperar URL: {}", e.getMessage());
            throw new RuntimeException("Falha ao processar redirecionamento", e);
        }
    }

    private String getJsonFromS3(String key) throws IOException {
        try (var objectResponse = s3Client.getObject(GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key + ".json")
                .build());
             BufferedReader reader = new BufferedReader(new InputStreamReader(objectResponse))) {


            return reader.lines().collect(Collectors.joining());
        }
    }
}
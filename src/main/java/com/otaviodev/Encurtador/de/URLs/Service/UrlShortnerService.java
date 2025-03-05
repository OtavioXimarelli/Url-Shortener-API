package com.otaviodev.Encurtador.de.URLs.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.Map;
import java.util.UUID;

@Service
public class UrlShortnerService {
    private final S3Client s3Client;
    private final String bucketName = "otavio-urlshortner-bucket";
    private final ObjectMapper objectMapper;

    public UrlShortnerService(S3Client s3Client, ObjectMapper objectMapper) {
        this.s3Client = s3Client;
        this.objectMapper = objectMapper;
    }

    public String shortenUrl(String url) throws JsonProcessingException {

        String key = UUID.randomUUID().toString().substring(0, 8);


        String actualUrl = url;
        if (url.trim().startsWith("url")) {
            try {
                Map<String, String> urlMap = objectMapper.readValue(url, new TypeReference<Map<String, String>>() {
                });
                if (urlMap.containsKey("url")) {
                    actualUrl = urlMap.get("url");
                }
            } catch (Exception ex) {
                System.out.println("Erro ao processar redirecionamento");
            }


            Map<String, String> urlData = Map.of(
                    "key", key,
                    "url", actualUrl
            );

            String jsonContent = objectMapper.writeValueAsString(urlData);
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key + "json")
                            .build(),
                    RequestBody.fromString(jsonContent)
            );
        }
        return key;
    }
}
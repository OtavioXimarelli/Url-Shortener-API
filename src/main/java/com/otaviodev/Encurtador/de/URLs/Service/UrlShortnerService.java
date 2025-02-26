package com.otaviodev.Encurtador.de.URLs.Service;


import com.fasterxml.jackson.core.JsonProcessingException;
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

    public UrlShortnerService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String shortenUrl(String url) throws JsonProcessingException {
        String shortCode = UUID.randomUUID().toString().substring(0, 8);

        Map<String, Object> urlData = Map.of(
                "shortKey", shortCode,
                "url", url
        );

        String json = new ObjectMapper().writeValueAsString(urlData);

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(shortCode + ".json")
                        .build(),
                RequestBody.fromString(json)
        );
        return shortCode;
    }
}

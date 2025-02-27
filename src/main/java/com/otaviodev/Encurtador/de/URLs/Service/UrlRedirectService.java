package com.otaviodev.Encurtador.de.URLs.Service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.Map;


@Getter
@Setter
@Service
public class UrlRedirectService {
    private final S3Client s3Client;
    private static final Logger logger = LoggerFactory.getLogger(UrlRedirectService.class);
    private final StringRedisTemplate redisTemplate;
    private final String bucketName = "otavio-urlshortner-bucket";


    public UrlRedirectService(S3Client s3Client, StringRedisTemplate stringRedisTemplate, StringRedisTemplate redisTemplate) {
        this.s3Client = s3Client;
        this.redisTemplate = redisTemplate;
    }


    public String getUrl(String key, HttpServletResponse response) throws IOException {

        String cachedUrl = redisTemplate.opsForValue().get(key);
        if (cachedUrl != null) {
            logger.info("Redirecting to {}", cachedUrl);
            return cachedUrl;
        }
        try {
            logger.info("Redirecting to {}", key);
            var objectResponse = s3Client.getObject(
                    GetObjectRequest
                            .builder()
                            .bucket(bucketName)
                            .key(key + ".json")
                            .build());

            BufferedReader reader = new BufferedReader(new InputStreamReader(objectResponse));
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }

            logger.info("Json content : {}",  jsonContent.toString());

            ObjectMapper jsonMapper = new ObjectMapper();
            Map<String, String> urlData = jsonMapper.readValue(jsonContent.toString(), Map.class);

            String originalUrl = (String) urlData.get("url");
            if (originalUrl == null) {
                throw new RuntimeException("Url not found in json");
            }
            redisTemplate.opsForValue().set(key, originalUrl, Duration.ofHours(1));
            logger.info("URL cached to redis: {}", key);
            return originalUrl;
        } catch (S3Exception e) {
            logger.error("Error retrieving URL from S3: {}", e.getMessage());
            throw new RuntimeException("Unable to get URL from s3", e); //TODO:melhorar o tramento de erros
        }

    }


}



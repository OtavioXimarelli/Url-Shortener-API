package com.otaviodev.Encurtador.de.URLs.Service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

@Service
public class UrlRedirectService {
    private final S3Client s3Client;
    private final String bucketName = "otavio-urlshortner-bucket";


    public UrlRedirectService(S3Client s3Client) {
        this.s3Client = s3Client;
    }


    public String getUrl(String key, HttpServletResponse response) throws IOException {
        try {
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

            ObjectMapper jsonMapper = new ObjectMapper();
            Map<String, String> urlData = jsonMapper.readValue(jsonContent.toString(), Map.class);
            return urlData.get("url");
        } catch (S3Exception e) {
            throw new RuntimeException("Unable to get URL from s3", e); //TODO:melhorar o tramento de erros
        }

    }


}



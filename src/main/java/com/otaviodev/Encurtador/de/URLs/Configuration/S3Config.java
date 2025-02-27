package com.otaviodev.Encurtador.de.URLs.Configuration;


import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {


    @Bean
    public S3Client s3Client() {
        Dotenv dotenv = Dotenv.load();
        String accessKey = dotenv.get("ACCESS_KEY");
        String secretKey = dotenv.get("SECRET_KEY");


        if (accessKey == null || secretKey == null) {
            throw new IllegalStateException("aws ACCESS_KEY and SECRET_KEY must be set in the .env");
        }

        return S3Client.builder()
                .region(Region.US_EAST_1) // Define a região do S3
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .build();
    }

}

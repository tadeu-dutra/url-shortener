package com.example.service;

import java.util.UUID;

import com.example.dto.UrlRequest;
import com.example.exception.UrlShortenerException;
import com.example.model.UrlData;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class UrlShortenerService {

    private final S3Client s3Client;
    private final ObjectMapper objectMapper;

    public UrlShortenerService(S3Client s3Client, ObjectMapper objectMapper) {
        this.s3Client = s3Client;
        this.objectMapper = objectMapper;
    }

    public String shortenUrl(UrlRequest urlRequest) {

        String shortUrlCode = UUID.randomUUID().toString().substring(0, 8);
        UrlData urlData = new UrlData(urlRequest.originalUrl(), urlRequest.expirationTime());

        try {
            String urlDataJson = objectMapper.writeValueAsString(urlData);

            PutObjectRequest request = PutObjectRequest.builder()
                .bucket("url-shortener-storage-bucket")
                .key(shortUrlCode + ".json")
                .build();
            
            s3Client.putObject(request, RequestBody.fromString(urlDataJson));
            
        } catch (Exception e) {
            throw new UrlShortenerException("Error saving data to S3", e);
        }

        return shortUrlCode;
    }
}

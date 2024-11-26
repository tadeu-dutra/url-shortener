package com.example;

import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.example.dto.UrlRequest;
import com.example.dto.UrlResponse;
import com.example.exception.UrlShortenerException;
import com.example.service.UrlShortenerService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ShortUrlHandler implements RequestHandler<Map<String, Object>, UrlResponse> {

    private final UrlShortenerService urlShortenerService;
    private final ObjectMapper objectMapper;

    public ShortUrlHandler(UrlShortenerService urlShortenerService, ObjectMapper objectMapper) {
        this.urlShortenerService = urlShortenerService;
        this.objectMapper = objectMapper;
    }

    @Override
    public UrlResponse handleRequest(Map<String, Object> input, Context context) {
        
        String body = input.get("body").toString();
        UrlRequest urlRequest;

        try {
            urlRequest = objectMapper.readValue(body, UrlRequest.class);
        } catch (Exception e) {
            throw new UrlShortenerException("Error while parsing JSON body", e);
        }

        return new UrlResponse(urlShortenerService.shortenUrl(urlRequest));
    }


    

    
}

package org.ehealth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
public class AIService {

    private final RestTemplate restTemplate;
    private final String apiUrl;
    private final String apiKey;
    private final String model;

    public AIService(@Value("${ai.provider.url}") String apiUrl,
                     @Value("${ai.provider.apiKey}") String apiKey,
                     @Value("${ai.model}") String model) {
        this.restTemplate = new RestTemplate();
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.model = model;
    }

    public String getReply(String userMessage) {
        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("messages", List.of(Map.of("role", "user", "content", userMessage)));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        System.out.println("Using model: " + model);
        System.out.println("API URL: " + apiUrl);
        System.out.println("API key (first 8 chars): " + apiKey.substring(0, 8));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Object choices = response.getBody().get("choices");
            if (choices instanceof List list && !list.isEmpty()) {
                Object first = list.get(0);
                if (first instanceof Map m) {
                    Object message = m.get("message");
                    if (message instanceof Map mm) return (String) mm.get("content");
                    Object text = m.get("text");
                    if (text instanceof String) return (String) text;
                }
            }
        }

        return "Sorry, I couldn't generate a reply.";
    }
}

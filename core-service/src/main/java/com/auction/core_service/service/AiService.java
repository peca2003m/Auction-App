package com.auction.core_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AiService {

    @Value("${anthropic.api-key}")
    private String apiKey;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generateAuctionDescription(String title, String category) {
        try {
            String prompt = String.format(
                    "Generate a compelling auction description for '%s' in category '%s'. " +
                            "CRITICAL REQUIREMENT: The description MUST NOT exceed 180 characters under any circumstances. " +
                            "No introductory text, no bullet points, no title. Plain text only.",
                    title, category
            );

            Map<String, Object> body = Map.of(
                    "model", "claude-haiku-4-5",
                    "max_tokens", 200,
                    "messages", List.of(
                            Map.of("role", "user", "content", prompt)
                    )
            );

            String jsonBody = objectMapper.writeValueAsString(body);

            Request request = new Request.Builder()
                    .url("https://api.anthropic.com/v1/messages")
                    .post(RequestBody.create(jsonBody, MediaType.parse("application/json")))
                    .addHeader("x-api-key", apiKey)
                    .addHeader("anthropic-version", "2023-06-01")
                    .addHeader("content-type", "application/json")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String responseBody = response.body().string();
                Map<String, Object> parsed = objectMapper.readValue(responseBody, Map.class);
                List<Map<String, Object>> content = (List<Map<String, Object>>) parsed.get("content");
                return (String) content.get(0).get("text");
            }

        } catch (Exception e) {
            log.error("Error calling Anthropic API", e);
            return "Description could not be generated.";
        }
    }
}
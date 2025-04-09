package org.bbqqvv.backendeducation.service.impl;

import org.bbqqvv.backendeducation.service.ChatBotService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class ChatBotServiceImpl implements ChatBotService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final WebClient webClient;

    public ChatBotServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<String> generateReply(String prompt) {
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );

        return webClient.post()
                .uri("/models/gemini-2.0-flash:generateContent?key=" + apiKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    try {
                        List candidates = (List) response.get("candidates");
                        Map candidate = (Map) candidates.get(0);
                        Map content = (Map) candidate.get("content");
                        List parts = (List) content.get("parts");
                        Map firstPart = (Map) parts.get(0);
                        return (String) firstPart.get("text");
                    } catch (Exception e) {
                        return "Không thể phân tích phản hồi từ Gemini.";
                    }
                });
    }
}

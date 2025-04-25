package org.bbqqvv.backendeducation.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bbqqvv.backendeducation.dto.response.IntentResult;
import org.bbqqvv.backendeducation.intent.IntentHandler;
import org.bbqqvv.backendeducation.service.ChatBotService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Service
public class ChatBotServiceImpl implements ChatBotService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<IntentHandler> intentHandlers;

    public ChatBotServiceImpl(WebClient webClient, List<IntentHandler> intentHandlers) {
        this.webClient = webClient;
        this.intentHandlers = intentHandlers;
    }

    @Override
    public Mono<String> generateReply(String prompt) {
        return analyzeIntent(prompt)
                .flatMap(intentResult -> {
                    if (intentResult == null || intentResult.getIntent().equalsIgnoreCase("unknown")) {
                        return callGemini(prompt); // fallback khi không rõ intent
                    }

                    return intentHandlers.stream()
                            .filter(handler -> handler.supports(intentResult.getIntent()))
                            .findFirst()
                            .map(handler -> handler.handle(intentResult))
                            .orElse(callGemini(prompt));
                })
                .onErrorResume(e -> callGemini(prompt));
    }

    private Mono<IntentResult> analyzeIntent(String prompt) {
        return Mono.defer(() -> {
            try {
                String systemPrompt = getPromptFromFile().formatted(prompt);
                Map<String, Object> requestBody = createRequestBody(systemPrompt);

                return webClient.post()
                        .uri("/models/gemini-2.0-flash:generateContent?key=" + apiKey)
                        .bodyValue(requestBody)
                        .retrieve()
                        .bodyToMono(Map.class)
                        .map(this::parseIntentResult);
            } catch (IOException e) {
                e.printStackTrace();
                return Mono.just(new IntentResult("unknown", null, null,null));
            }
        });
    }

    private String getPromptFromFile() throws IOException {
        return new String(Files.readAllBytes(Paths.get("src/main/resources/prompt.txt")));
    }

    private Map<String, Object> createRequestBody(String promptText) {
        return Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", promptText)
                        ))
                )
        );
    }

    private IntentResult parseIntentResult(Map response) {
        try {
            List candidates = (List) response.get("candidates");
            if (candidates == null || candidates.isEmpty()) return new IntentResult("unknown", null, null,null);

            Map candidate = (Map) candidates.get(0);
            Map content = (Map) candidate.get("content");
            List parts = (List) content.get("parts");

            if (parts == null || parts.isEmpty()) return new IntentResult("unknown", null, null,null);

            Map firstPart = (Map) parts.get(0);
            String jsonText = (String) firstPart.get("text");

            // Nếu Gemini trả về JSON kèm markdown, loại bỏ chúng
            if (jsonText.startsWith("```")) {
                jsonText = jsonText.replaceAll("(?s)```(?:json)?\\s*(.*?)\\s*```", "$1").trim();
            }

            IntentResult result = objectMapper.readValue(jsonText, IntentResult.class);

            // Kiểm tra thêm để tránh className hoặc teacherName bị null nếu không phù hợp intent
            if ("list_students_by_class".equals(result.getIntent()) ||
                    "count_students_by_class".equals(result.getIntent()) ||
                    "count_teachers_by_class".equals(result.getIntent())) {
                if (result.getClassName() == null || result.getClassName().isBlank()) {
                    return new IntentResult("unknown", null, null,null);
                }
            } else if ("list_classes_of_teacher".equals(result.getIntent())) {
                if (result.getTeacherName() == null || result.getTeacherName().isBlank()) {
                    return new IntentResult("unknown", null, null,null);
                }
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new IntentResult("unknown", null, null,null);
        }
    }

    private Mono<String> callGemini(String prompt) {
        Map<String, Object> requestBody = createRequestBody(prompt);

        return webClient.post()
                .uri("/models/gemini-2.0-flash:generateContent?key=" + apiKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(this::parseGeminiResponse);
    }

    private String parseGeminiResponse(Map response) {
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
    }
}

package org.bbqqvv.backendeducation.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bbqqvv.backendeducation.dto.response.IntentResult;
import org.bbqqvv.backendeducation.entity.User;
import org.bbqqvv.backendeducation.repository.ReactiveUserRepository;
import org.bbqqvv.backendeducation.service.ChatBotService;
import org.bbqqvv.backendeducation.util.ResponseTemplateUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatBotServiceImpl implements ChatBotService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final WebClient webClient;
    private final ReactiveUserRepository reactiveUserRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ChatBotServiceImpl(WebClient webClient, ReactiveUserRepository reactiveUserRepository) {
        this.webClient = webClient;
        this.reactiveUserRepository = reactiveUserRepository;
    }

    @Override
    public Mono<String> generateReply(String prompt) {
        return analyzeIntent(prompt)
                .flatMap(intentResult -> {
                    switch (intentResult.getIntent()) {
                        case "count_students_by_class":
                            return reactiveUserRepository.countByStudentClass(intentResult.getClassName())
                                    .map(count -> ResponseTemplateUtil.getCountStudentsByClass(intentResult.getClassName(), count));

                        case "list_students_by_class":
                            return reactiveUserRepository.findByStudentClass(intentResult.getClassName())
                                    .collectList()
                                    .map(users -> {
                                        if (users.isEmpty()) return "Mình không tìm thấy học sinh nào trong lớp " + intentResult.getClassName();
                                        String names = users.stream()
                                                .map(User::getFullName)
                                                .collect(Collectors.joining(", "));
                                        return ResponseTemplateUtil.getListStudentsByClass(intentResult.getClassName(), names);
                                    });

                        case "count_teachers_by_class":
                            return reactiveUserRepository.findByTeachingClassesContaining(intentResult.getClassName())
                                    .count()
                                    .map(count -> ResponseTemplateUtil.getCountTeachersByClass(intentResult.getClassName(), count));

                        case "list_classes_of_teacher":
                            return reactiveUserRepository.findByFullName(intentResult.getTeacherName())
                                    .collectList()
                                    .map(users -> {
                                        if (users.isEmpty()) return "Mình không tìm thấy giáo viên nào tên " + intentResult.getTeacherName();
                                        Set<String> classes = users.stream()
                                                .flatMap(u -> u.getTeachingClasses().stream())
                                                .collect(Collectors.toSet());
                                        return ResponseTemplateUtil.getListClassesOfTeacher(intentResult.getTeacherName(), classes);
                                    });

                        case "introduce_school":
                            return Mono.just(ResponseTemplateUtil.getRandomSchoolIntroduction());

                        default:
                            return callGemini(prompt);
                    }
                })
                .onErrorResume(e -> callGemini(prompt));
    }

    private Mono<IntentResult> analyzeIntent(String prompt) {
        String systemPrompt = """
        Hãy phân tích câu hỏi sau và trả về JSON theo đúng cấu trúc. Dưới đây là một số ví dụ:

        - Đếm số học sinh trong lớp:
          {
            "intent": "count_students_by_class",
            "className": "12A1"
          }

        - Liệt kê học sinh của lớp:
          {
            "intent": "list_students_by_class",
            "className": "11B2"
          }

        - Đếm số giáo viên dạy lớp:
          {
            "intent": "count_teachers_by_class",
            "className": "10C3"
          }

        - Lấy danh sách lớp mà giáo viên đang dạy:
          {
            "intent": "list_classes_of_teacher",
            "teacherName": "Nguyễn Văn A"
          }

        - Giới thiệu về trường:
          {
            "intent": "introduce_school"
          }

        Nếu không xác định được, trả về:
        {
          "intent": "unknown"
        }

        Câu hỏi: %s
        """.formatted(prompt);

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", systemPrompt)
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
                        String jsonText = (String) firstPart.get("text");

                        System.out.println("Gemini raw response:\n" + jsonText);

                        // ✅ Gỡ bỏ markdown nếu có
                        if (jsonText.startsWith("```")) {
                            jsonText = jsonText.replaceAll("(?s)```(?:json)?\\s*(.*?)\\s*```", "$1").trim();
                        }

                        return objectMapper.readValue(jsonText, IntentResult.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return new IntentResult("unknown", null, null);
                    }
                });
    }

    private Mono<String> callGemini(String prompt) {
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

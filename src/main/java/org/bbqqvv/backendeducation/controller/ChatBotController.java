package org.bbqqvv.backendeducation.controller;

import org.bbqqvv.backendeducation.dto.request.ChatBotRequest;
import org.bbqqvv.backendeducation.dto.response.ChatBotResponse;
import org.bbqqvv.backendeducation.service.ChatBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/chatbot")
public class ChatBotController {

    private final ChatBotService chatBotService;

    @Autowired
    public ChatBotController(ChatBotService chatBotService) {
        this.chatBotService = chatBotService;
    }

    @PostMapping("/ask")
    public Mono<ChatBotResponse> ask(@RequestBody ChatBotRequest request) {
        return chatBotService.generateReply(request.getMessage())
                .map(reply -> ChatBotResponse.builder().reply(reply).build());
    }
}
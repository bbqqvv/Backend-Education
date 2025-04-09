package org.bbqqvv.backendeducation.service;

import org.bbqqvv.backendeducation.entity.User;
import reactor.core.publisher.Mono;

public interface ChatBotService  {
    Mono<String> generateReply(String prompt);
}

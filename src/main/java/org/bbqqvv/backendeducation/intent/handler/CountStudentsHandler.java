package org.bbqqvv.backendeducation.intent.handler;

import org.bbqqvv.backendeducation.dto.response.IntentResult;
import org.bbqqvv.backendeducation.intent.IntentHandler;
import org.bbqqvv.backendeducation.repository.reactive.ReactiveUserRepository;
import org.bbqqvv.backendeducation.util.ResponseTemplateUtil;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CountStudentsHandler implements IntentHandler {

    private final ReactiveUserRepository userRepository;

    public CountStudentsHandler(ReactiveUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(String intent) {
        return "count_students_by_class".equals(intent);
    }

    @Override
    public Mono<String> handle(IntentResult intentResult) {
        return userRepository.countByStudentClass(intentResult.getClassName())
                .map(count -> ResponseTemplateUtil.getCountStudentsByClass(intentResult.getClassName(), count));
    }
}
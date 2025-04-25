package org.bbqqvv.backendeducation.intent.handler;

import org.bbqqvv.backendeducation.dto.response.IntentResult;
import org.bbqqvv.backendeducation.intent.IntentHandler;
import org.bbqqvv.backendeducation.repository.reactive.ReactiveUserRepository;
import org.bbqqvv.backendeducation.util.ResponseTemplateUtil;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CountTeachersHandler implements IntentHandler {

    private final ReactiveUserRepository userRepository;

    public CountTeachersHandler(ReactiveUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(String intent) {
        return "count_teachers_by_class".equals(intent);
    }

    @Override
    public Mono<String> handle(IntentResult intentResult) {
        return userRepository.findByTeachingClassesContaining(intentResult.getClassName())
                .count()
                .map(count -> ResponseTemplateUtil.getCountTeachersByClass(intentResult.getClassName(), count));
    }
}

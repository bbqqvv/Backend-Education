package org.bbqqvv.backendeducation.intent.handler;
import org.bbqqvv.backendeducation.dto.response.IntentResult;
import org.bbqqvv.backendeducation.entity.User;
import org.bbqqvv.backendeducation.intent.IntentHandler;
import org.bbqqvv.backendeducation.repository.reactive.ReactiveUserRepository;
import org.bbqqvv.backendeducation.util.ResponseTemplateUtil;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Component
public class ListStudentsHandler implements IntentHandler {

    private final ReactiveUserRepository userRepository;

    public ListStudentsHandler(ReactiveUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(String intent) {
        return "list_students_by_class".equals(intent);
    }

    @Override
    public Mono<String> handle(IntentResult intentResult) {
        return userRepository.findByStudentClass(intentResult.getClassName())
                .collectList()
                .map(users -> {
                    if (users.isEmpty()) return "Mình không tìm thấy học sinh nào trong lớp " + intentResult.getClassName();
                    String names = users.stream()
                            .map(User::getFullName)
                            .collect(Collectors.joining(", "));
                    return ResponseTemplateUtil.getListStudentsByClass(intentResult.getClassName(), names);
                });
    }
}

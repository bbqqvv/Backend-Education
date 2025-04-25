package org.bbqqvv.backendeducation.intent.handler;

import org.bbqqvv.backendeducation.dto.response.IntentResult;
import org.bbqqvv.backendeducation.intent.IntentHandler;
import org.bbqqvv.backendeducation.repository.reactive.ReactiveUserRepository;
import org.bbqqvv.backendeducation.util.ResponseTemplateUtil;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ListTeacherClassesHandler implements IntentHandler {

    private final ReactiveUserRepository userRepository;

    public ListTeacherClassesHandler(ReactiveUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(String intent) {
        return "list_classes_of_teacher".equals(intent);
    }

    @Override
    public Mono<String> handle(IntentResult intentResult) {
        return userRepository.findByFullName(intentResult.getTeacherName())
                .collectList()
                .map(users -> {
                    if (users.isEmpty()) return "Mình không tìm thấy giáo viên nào tên " + intentResult.getTeacherName();
                    Set<String> classes = users.stream()
                            .flatMap(user -> user.getTeachingClasses().stream())
                            .collect(Collectors.toSet());
                    return ResponseTemplateUtil.getListClassesOfTeacher(intentResult.getTeacherName(), classes);
                });
    }
}

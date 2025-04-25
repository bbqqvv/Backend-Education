package org.bbqqvv.backendeducation.intent.handler;

import org.bbqqvv.backendeducation.dto.response.IntentResult;
import org.bbqqvv.backendeducation.entity.ExamSchedule;
import org.bbqqvv.backendeducation.intent.IntentHandler;
import org.bbqqvv.backendeducation.repository.reactive.ReactiveExamScheduleRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.stream.Collectors;

@Component
public class GetExamScheduleHandler implements IntentHandler {

    private final ReactiveExamScheduleRepository examScheduleRepository;

    public GetExamScheduleHandler(ReactiveExamScheduleRepository examScheduleRepository) {
        this.examScheduleRepository = examScheduleRepository;
    }

    @Override
    public boolean supports(String intent) {
        return "get_exam_schedule".equals(intent);
    }

    @Override
    public Mono<String> handle(IntentResult intentResult) {
        String className = intentResult.getClassName();

        Flux<ExamSchedule> examSchedulesFlux = examScheduleRepository.findByClassName(className);

        return examSchedulesFlux
                .collectList()
                .map(exams -> {
                    if (exams.isEmpty()) {
                        return "Mình không tìm thấy lịch thi cho lớp " + className;
                    }
                    String examDetails = exams.stream()
                            .map(exam -> String.format(
                                    "Môn: %s, Ngày: %s, Giờ: %s - %s, Phòng: %s, Giám thị: %s",
                                    exam.getSubject(),
                                    exam.getExamDate(),
                                    exam.getStartTime(),
                                    exam.getEndTime(),
                                    exam.getExamRoom(),
                                    exam.getTeacherName()
                            ))
                            .collect(Collectors.joining("\n"));
                    return "Lịch thi của lớp " + className + ":\n" + examDetails;
                });
    }
}

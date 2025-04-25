package org.bbqqvv.backendeducation.repository.reactive;

import org.bbqqvv.backendeducation.entity.ExamSchedule;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@Repository
public interface ReactiveExamScheduleRepository extends ReactiveMongoRepository<ExamSchedule, String> {

    Flux<ExamSchedule> findByClassName(String className);

    Flux<ExamSchedule> findByTeacherId(String teacherId);

    Flux<ExamSchedule> findByTeacherIdAndExamDate(String teacherId, LocalDate examDate);

    Flux<ExamSchedule> findByClassNameAndExamDate(String className, LocalDate examDate);
}

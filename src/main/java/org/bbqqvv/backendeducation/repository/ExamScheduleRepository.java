package org.bbqqvv.backendeducation.repository;

import org.bbqqvv.backendeducation.entity.ExamSchedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExamScheduleRepository extends MongoRepository<ExamSchedule, String> {
    List<ExamSchedule> findByClassName(String className);
    List<ExamSchedule> findByTeacherId(String teacherId);
    List<ExamSchedule> findByTeacherIdAndExamDate(String teacherId, LocalDate date);
    List<ExamSchedule> findByClassNameAndExamDate(String className, LocalDate examDate);
}
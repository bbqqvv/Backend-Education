package org.bbqqvv.backendeducation.repository;

import org.bbqqvv.backendeducation.entity.ExamSchedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExamScheduleRepository extends MongoRepository<ExamSchedule, String> {
    List<ExamSchedule> findByClassName(String className);
    List<ExamSchedule> findByTeacherName(String teacherName);
    List<ExamSchedule> findByClassNameAndExamDate(String className, LocalDate examDate);
    List<ExamSchedule> findByTeacherNameAndExamDate(String teacherName, LocalDate examDate);

}
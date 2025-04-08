package org.bbqqvv.backendeducation.repository;

import org.bbqqvv.backendeducation.entity.TimeTable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface TimeTableRepository extends MongoRepository<TimeTable, String> {
    List<TimeTable> findByClassName(String className);
    List<TimeTable> findByClassNameAndDayOfWeek(String className, String dayOfWeek);

    List<TimeTable> findByTeacherName(String teacherName);
}

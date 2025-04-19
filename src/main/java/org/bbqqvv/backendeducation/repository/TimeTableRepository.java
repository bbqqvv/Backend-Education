package org.bbqqvv.backendeducation.repository;

import org.bbqqvv.backendeducation.entity.DayOfWeekEnum;
import org.bbqqvv.backendeducation.entity.TimeTable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TimeTableRepository extends MongoRepository<TimeTable, String> {
    List<TimeTable> findByClassName(String className);
    List<TimeTable> findByClassNameAndDayOfWeek(String className, DayOfWeekEnum dayOfWeek);
    List<TimeTable> findByTeacherName(String teacherName);

    // ✅ Thêm dòng này:
    List<TimeTable> findByTeacherNameAndDayOfWeek(String teacherName, DayOfWeekEnum dayOfWeek);
}

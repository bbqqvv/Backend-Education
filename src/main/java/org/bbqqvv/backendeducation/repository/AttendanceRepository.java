package org.bbqqvv.backendeducation.repository;

import org.bbqqvv.backendeducation.entity.AttendanceRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends MongoRepository<AttendanceRecord, String> {
    Optional<AttendanceRecord> findById(String attendanceId);

    List<AttendanceRecord> findByClassName(String className);

    List<AttendanceRecord> findByStudentId(String studentId);

    List<AttendanceRecord> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}

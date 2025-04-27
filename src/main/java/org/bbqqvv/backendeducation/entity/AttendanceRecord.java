package org.bbqqvv.backendeducation.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("attendance")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceRecord {

    @Id
    private String id;  // ID của buổi điểm danh, Mongo sẽ tự động tạo nếu không có.

    private String className;  // Tên lớp học

    private String studentId;  // ID học sinh

    private String userCode;

    private String studentName;  // Tên học sinh

    private RecordStatus status;  // Trạng thái điểm danh (Có mặt, Vắng mặt, Muộn)

    private LocalDateTime date;  // Ngày điểm danh

    private LocalDateTime createdDate;  // Thời gian tạo bản ghi điểm danh

}

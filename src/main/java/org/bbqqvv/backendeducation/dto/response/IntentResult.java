package org.bbqqvv.backendeducation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntentResult {
    private String intent;     // mã intent như: count_students_by_class
    private String className;  // ví dụ: "12A1"
    private String teacherName;

}

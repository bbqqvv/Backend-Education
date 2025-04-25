package org.bbqqvv.backendeducation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntentResult {
    private String intent;
    private String className;
    private String teacherName;
    private String category;

}

package org.bbqqvv.backendeducation.dto.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UpdateProfileRequest {
    private String address;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private String fatherName;
    private String motherName;
    private String fatherPhoneNumber;
    private String motherPhoneNumber;
}
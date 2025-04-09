package org.bbqqvv.backendeducation.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class UserProfileResponse {
    private String id;
    private String userId;
    private String address;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private String nationality;
    private String socialMediaLinks;
    private String emergencyContact;
    private String fatherName;
    private String motherName;
    private String fatherPhoneNumber;
    private String motherPhoneNumber;
}
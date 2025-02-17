package org.bbqqvv.backendeducation.dto.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ChangeProfileRequest {
    private String address;            // Địa chỉ của người dùng
    private String phoneNumber;        // Số điện thoại của người dùng
    private LocalDate dateOfBirth;     // Ngày sinh
    private String gender;             // Giới tính (Nam/Nữ/Khác)
    private String profilePicture;     // Ảnh đại diện (URL hoặc base64)
    private String nationality;        // Quốc tịch của người dùng
    private String socialMediaLinks;   // Các liên kết mạng xã hội (ví dụ: Facebook, LinkedIn)
    private String emergencyContact;   // Thông tin liên lạc khẩn cấp
    private String fatherName;         // Tên của cha
    private String motherName;         // Tên của mẹ
    private String fatherPhoneNumber;  // Số điện thoại của cha
    private String motherPhoneNumber;  // Số điện thoại của mẹ
}

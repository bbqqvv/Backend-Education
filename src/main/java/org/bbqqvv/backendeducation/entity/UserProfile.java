package org.bbqqvv.backendeducation.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {

    @Field(name = "user_id")
    private String userId; // Liên kết với ID người dùng trong bảng User (mã người dùng)

    @Field(name = "address")
    private String address; // Địa chỉ của người dùng

    @Field(name = "phone_number")
    private String phoneNumber; // Số điện thoại của người dùng

    @Field(name = "date_of_birth")
    private LocalDate dateOfBirth; // Ngày sinh của người dùng

    @Field(name = "gender")
    private String gender; // Giới tính (Nam/Nữ/Khác)

    @Field(name = "profile_picture")
    private String profilePicture; // Ảnh đại diện (URL hoặc base64)

    @Field(name = "father_name")
    private String fatherName; // Tên cha của người dùng

    @Field(name = "mother_name")
    private String motherName; // Tên mẹ của người dùng

    @Field(name = "father_phone_number")
    private String fatherPhoneNumber; // Số điện thoại của cha

    @Field(name = "mother_phone_number")
    private String motherPhoneNumber; // Số điện thoại của mẹ

    @Field(name = "updated_at")
    private LocalDateTime updatedAt; // Thời gian cập nhật thông tin

    // Cập nhật thông tin của người dùng
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

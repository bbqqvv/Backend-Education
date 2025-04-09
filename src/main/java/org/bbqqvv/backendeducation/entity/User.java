package org.bbqqvv.backendeducation.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Set;

@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    private String id;

    @Field(name = "email")
    private String email;

    @Field(name = "password")
    private String password;

    @Field(name = "full_name")
    private String fullName;

    @Field(name = "student_code")
    private String studentCode;

    @Field(name = "student_class")
    private String studentClass;

    @Field("profile")
    private UserProfile profile;

    @Field(name = "roles")
    private Set<Role> roles; // Role Enum, có thể là Set nếu muốn một người dùng có nhiều quyền

    @Field(name = "created_at")
    private LocalDateTime createdAt;

    @Field(name = "updated_at")
    private LocalDateTime updatedAt;


    // Tạo tài khoản mặc định khi khởi tạo
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.roles == null || this.roles.isEmpty()) {
            this.roles = Set.of(Role.ROLE_STUDENT); // Mặc định nếu chưa có giá trị, sẽ gán ROLE_STUDENT
        }
    }

    // Cập nhật tài khoản
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

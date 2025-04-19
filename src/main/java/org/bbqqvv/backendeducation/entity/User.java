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

    @Field(name = "user_code")
    private String userCode;

    @Field(name = "student_class")
    private String studentClass; // Học sinh dùng

    @Field(name = "teaching_classes")
    private Set<String> teachingClasses; // Giáo viên dùng

    @Field(name = "profile")
    private UserProfile profile;

    @Field(name = "roles")
    private Set<Role> roles;

    @Field(name = "created_at")
    private LocalDateTime createdAt;

    @Field(name = "updated_at")
    private LocalDateTime updatedAt;

    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.roles == null || this.roles.isEmpty()) {
            this.roles = Set.of(Role.ROLE_STUDENT);
        }
    }

    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

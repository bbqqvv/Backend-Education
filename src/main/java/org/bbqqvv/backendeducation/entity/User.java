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
public class User {

    @Id
    private String id;

    @Field(name = "username")
    private String username;

    @Field(name = "password")
    private String password;

    @Field(name = "email")
    private String email;

    @Field(name = "role")
    private Set<Role> authorities;

    @Field(name = "createdAt")
    private LocalDateTime createdAt;

    @Field(name = "updatedAt")
    private LocalDateTime updatedAt;

    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.authorities == null) {
            this.authorities = Set.of(Role.ROLE_USER); // Mặc định ROLE_USER
        }
    }

    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Constructor với các tham số
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        onCreate();  // Gọi onCreate khi tạo user mới
    }

    public User(String username, String password, String email, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.authorities = Set.of(role);
        onCreate();  // Gọi onCreate khi tạo user mới với quyền cụ thể
    }
}

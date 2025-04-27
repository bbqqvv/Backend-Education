package org.bbqqvv.backendeducation.entity;

import lombok.*;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDate;
import java.util.List;

@Document(collection = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {

    @Id
    private String id;

    @Field(name = "user_id")
    private String userId;

    @Field(name = "address")
    private String address;

    @Field(name = "phone_number")
    private String phoneNumber;

    @Field(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Field(name = "gender")
    private String gender;

    @Field(name = "profile_picture")
    private String profilePicture;

    @Field(name = "face_images")
    private List<String> faceImages;

    @Field(name = "father_name")
    private String fatherName;

    @Field(name = "mother_name")
    private String motherName;

    @Field(name = "father_phone_number")
    private String fatherPhoneNumber;

    @Field(name = "mother_phone_number")
    private String motherPhoneNumber;
}

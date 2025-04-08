package org.bbqqvv.backendeducation.entity;

import lombok.*;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "newsletter_likes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsletterLike {

    @Id
    private String id;

    private String newsletterId;
    private String userId;

    @CreatedDate
    private LocalDateTime likedAt;
}

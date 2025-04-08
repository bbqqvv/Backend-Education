package org.bbqqvv.backendeducation.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "newsletter")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Newsletter {
    @Id
    private String id;

    private String title;
    private String excerpt;
    private String content;
    private List<String> contentImages;

    private String author;
    private String category;
    private List<String> tags;
    private String thumbnailUrl;

    private int viewCount;

    // likeCount sẽ là cache, cập nhật định kỳ hoặc mỗi khi like/unlike
    private int likeCount;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private String createdBy;
    private String updatedBy;
}

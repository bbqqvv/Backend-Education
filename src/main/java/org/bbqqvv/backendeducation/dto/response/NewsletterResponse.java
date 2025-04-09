package org.bbqqvv.backendeducation.dto.response;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsletterResponse {
    private String id;
    private String title;
    private String content;
    private List<String> contentImages;
    private String excerpt;
    private String author;
    private String category;
    private List<String> tags;
    private String thumbnailUrl;
    private int viewCount;
    private int likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

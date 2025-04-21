package org.bbqqvv.backendeducation.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsletterRequest {

    private String title;
    private String content;
    private List<MultipartFile> contentImages;
    private String excerpt;
    private String category;
    private List<String> tags;
    private MultipartFile thumbnailUrl;
}

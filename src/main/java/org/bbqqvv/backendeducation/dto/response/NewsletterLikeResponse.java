package org.bbqqvv.backendeducation.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsletterLikeResponse {
    private String newsletterId;
    private String userId;
    private String name;
    private boolean liked;
    private int totalLikes;
}

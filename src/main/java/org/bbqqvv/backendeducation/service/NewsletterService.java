package org.bbqqvv.backendeducation.service;

import jakarta.validation.Valid;
import org.bbqqvv.backendeducation.dto.PageResponse;
import org.bbqqvv.backendeducation.dto.request.NewsletterRequest;
import org.bbqqvv.backendeducation.dto.response.NewsletterResponse;
import org.springframework.data.domain.Pageable;
import org.bbqqvv.backendeducation.dto.response.NewsletterLikeResponse;

public interface NewsletterService {
    NewsletterResponse addOrUpdateNewsletter(@Valid NewsletterRequest request);

    PageResponse<NewsletterResponse> getAllNewsletters(Pageable pageable);

    NewsletterResponse getNewsletterById(String id);

    void deleteNewsletter(String id);

    PageResponse<NewsletterResponse> getByCategory(String category, Pageable pageable);

    NewsletterLikeResponse likeNewsletter(String newsletterId);
}

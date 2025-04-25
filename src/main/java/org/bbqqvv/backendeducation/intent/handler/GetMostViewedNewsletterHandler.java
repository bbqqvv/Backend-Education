package org.bbqqvv.backendeducation.intent.handler;

import org.bbqqvv.backendeducation.dto.response.IntentResult;
import org.bbqqvv.backendeducation.entity.Newsletter;
import org.bbqqvv.backendeducation.intent.IntentHandler;
import org.bbqqvv.backendeducation.repository.reactive.ReactiveNewsletterRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class GetMostViewedNewsletterHandler implements IntentHandler {

    private final ReactiveNewsletterRepository newsletterRepository;

    public GetMostViewedNewsletterHandler(ReactiveNewsletterRepository newsletterRepository) {
        this.newsletterRepository = newsletterRepository;
    }

    @Override
    public boolean supports(String intent) {
        return "get_most_viewed_newsletter".equals(intent);
    }

    @Override
    public Mono<String> handle(IntentResult intentResult) {
        return newsletterRepository.findTopByOrderByViewCountDesc()
                .map(newsletter -> "Bài viết được xem nhiều nhất là: \"" + newsletter.getTitle() +
                        "\" với " + newsletter.getViewCount() + " lượt xem.")
                .defaultIfEmpty("Chưa có bài viết nào được xem.");
    }
}

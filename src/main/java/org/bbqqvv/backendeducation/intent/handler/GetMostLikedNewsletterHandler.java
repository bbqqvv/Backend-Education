package org.bbqqvv.backendeducation.intent.handler;

import org.bbqqvv.backendeducation.dto.response.IntentResult;
import org.bbqqvv.backendeducation.entity.Newsletter;
import org.bbqqvv.backendeducation.intent.IntentHandler;
import org.bbqqvv.backendeducation.repository.reactive.ReactiveNewsletterRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class GetMostLikedNewsletterHandler implements IntentHandler {

    private final ReactiveNewsletterRepository newsletterRepository;

    public GetMostLikedNewsletterHandler(ReactiveNewsletterRepository newsletterRepository) {
        this.newsletterRepository = newsletterRepository;
    }

    @Override
    public boolean supports(String intent) {
        return "get_most_liked_newsletter".equals(intent);
    }

    @Override
    public Mono<String> handle(IntentResult intentResult) {
        return newsletterRepository.findTopByOrderByLikeCountDesc()
                .map(newsletter -> "Bài viết được yêu thích nhất là: \"" + newsletter.getTitle() +
                        "\" với " + newsletter.getLikeCount() + " lượt thích.")
                .defaultIfEmpty("Hiện tại chưa có bài viết nào được thích.");
    }
}

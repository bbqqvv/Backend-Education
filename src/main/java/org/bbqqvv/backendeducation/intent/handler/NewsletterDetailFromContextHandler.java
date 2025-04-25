package org.bbqqvv.backendeducation.intent.handler;


import org.bbqqvv.backendeducation.dto.response.IntentResult;
import org.bbqqvv.backendeducation.intent.IntentHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class NewsletterDetailFromContextHandler implements IntentHandler {

    @Override
    public boolean supports(String intent) {
        return "get_newsletter_detail".equals(intent);
    }

    @Override
    public Mono<String> handle(IntentResult intentResult) {
        if (intentResult == null || intentResult.getNewsletterId() == null) {
            return Mono.just("Không tìm thấy bài viết chi tiết.");
        }
        return Mono.just("Chi tiết bài viết với ID: " + intentResult.getNewsletterId());
    }
}

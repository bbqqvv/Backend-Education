package org.bbqqvv.backendeducation.intent.handler;

import org.bbqqvv.backendeducation.dto.response.IntentResult;
import org.bbqqvv.backendeducation.entity.Newsletter;
import org.bbqqvv.backendeducation.intent.IntentHandler;
import org.bbqqvv.backendeducation.repository.reactive.ReactiveNewsletterRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.stream.Collectors;

@Component
public class GetNewslettersByCategoryHandler implements IntentHandler {

    private final ReactiveNewsletterRepository newsletterRepository;

    public GetNewslettersByCategoryHandler(ReactiveNewsletterRepository newsletterRepository) {
        this.newsletterRepository = newsletterRepository;
    }

    @Override
    public boolean supports(String intent) {
        return "get_newsletters_by_category".equals(intent);
    }

    @Override
    public Mono<String> handle(IntentResult intentResult) {
        String category = intentResult.getCategory();
        return newsletterRepository.findByCategoryIgnoreCase(category)
                .collectList()
                .map(list -> {
                    if (list.isEmpty()) {
                        return "Không tìm thấy bài viết nào trong chuyên mục \"" + category + "\".";
                    }
                    String titles = list.stream()
                            .map(Newsletter::getTitle)
                            .collect(Collectors.joining(", "));
                    return "Các bài viết thuộc chuyên mục \"" + category + "\": " + titles;
                });
    }
}

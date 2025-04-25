package org.bbqqvv.backendeducation.intent;

import org.bbqqvv.backendeducation.dto.response.IntentResult;
import reactor.core.publisher.Mono;

public interface IntentHandler {
    boolean supports(String intent);
    Mono<String> handle(IntentResult intentResult);
}

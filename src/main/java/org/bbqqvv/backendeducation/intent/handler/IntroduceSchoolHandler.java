package org.bbqqvv.backendeducation.intent.handler;


import org.bbqqvv.backendeducation.dto.response.IntentResult;
import org.bbqqvv.backendeducation.intent.IntentHandler;
import org.bbqqvv.backendeducation.util.ResponseTemplateUtil;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class IntroduceSchoolHandler implements IntentHandler {

    @Override
    public boolean supports(String intent) {
        return "introduce_school".equals(intent);
    }

    @Override
    public Mono<String> handle(IntentResult intentResult) {
        return Mono.just(ResponseTemplateUtil.getRandomSchoolIntroduction());
    }
}

package org.bbqqvv.backendeducation.repository.reactive;

import org.bbqqvv.backendeducation.entity.Newsletter;
import org.bbqqvv.backendeducation.entity.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveNewsletterRepository extends ReactiveMongoRepository<Newsletter, String> {
    Mono<Newsletter> findTopByOrderByLikeCountDesc();
    Mono<Newsletter> findTopByOrderByViewCountDesc();
    Flux<Newsletter> findByCategoryIgnoreCase(String category);

}

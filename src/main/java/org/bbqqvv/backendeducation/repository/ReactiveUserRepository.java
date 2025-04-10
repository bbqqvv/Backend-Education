package org.bbqqvv.backendeducation.repository;

import org.bbqqvv.backendeducation.entity.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ReactiveUserRepository extends ReactiveMongoRepository<User, String> {
    Mono<Long> countByStudentClass(String studentClass);
    Flux<User> findByStudentClass(String studentClass);
    Flux<User> findByTeachingClassesContaining(String className);
    Flux<User> findByFullName(String fullName);
}

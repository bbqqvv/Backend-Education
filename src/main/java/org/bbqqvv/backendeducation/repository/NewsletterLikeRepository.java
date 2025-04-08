package org.bbqqvv.backendeducation.repository;

import org.bbqqvv.backendeducation.entity.NewsletterLike;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsletterLikeRepository extends MongoRepository<NewsletterLike,String> {
    Optional<NewsletterLike> findByNewsletterIdAndUserId(String newsletterId, String id);
}

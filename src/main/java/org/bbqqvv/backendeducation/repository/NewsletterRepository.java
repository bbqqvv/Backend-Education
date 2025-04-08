package org.bbqqvv.backendeducation.repository;

import org.bbqqvv.backendeducation.entity.Newsletter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsletterRepository extends MongoRepository<Newsletter, String> {
    Page<Newsletter> findByCategoryIgnoreCase(String category, Pageable pageable);

}

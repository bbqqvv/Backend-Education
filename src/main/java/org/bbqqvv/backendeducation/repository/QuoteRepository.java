package org.bbqqvv.backendeducation.repository;

import org.bbqqvv.backendeducation.entity.Quote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuoteRepository extends MongoRepository<Quote, String> {
}

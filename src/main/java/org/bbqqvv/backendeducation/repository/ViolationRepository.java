package org.bbqqvv.backendeducation.repository;

import org.bbqqvv.backendeducation.entity.Violation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViolationRepository extends MongoRepository<Violation, String> {
    Page<Violation> findByUserCode(String userCode, Pageable pageable);
}

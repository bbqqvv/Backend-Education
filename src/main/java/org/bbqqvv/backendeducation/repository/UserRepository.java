package org.bbqqvv.backendeducation.repository;

import org.bbqqvv.backendeducation.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
	User findByUsername(String username);
	boolean existsByUsername(String username);
}

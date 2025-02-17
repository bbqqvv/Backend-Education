package org.bbqqvv.backendeducation.repository;

import org.bbqqvv.backendeducation.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
//	User findByUsername(String username);
//	boolean existsByUsername(String username);

	Optional<User> findByEmail(String email);
	boolean existsByEmail(String email);
}

package org.bbqqvv.backendeducation.repository;

import org.bbqqvv.backendeducation.entity.Role;
import org.bbqqvv.backendeducation.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
	List<User> findByRolesAndStudentClass(Role role, String studentClass);
	Optional<User> findByEmail(String email);
	boolean existsByEmail(String email);
	Optional<User> findByStudentClass(String studentClass);
    List<User> findAllByStudentClass(String className);
	Optional<User> findByUserCode(String userCode);
}

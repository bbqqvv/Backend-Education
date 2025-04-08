package org.bbqqvv.backendeducation.repository;

import org.bbqqvv.backendeducation.entity.LeaveRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRequestRepository extends MongoRepository<LeaveRequest, String> {
    List<LeaveRequest> findBySenderName(String fullName);
}

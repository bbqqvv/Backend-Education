package org.bbqqvv.backendeducation.repository;

import org.bbqqvv.backendeducation.entity.LeaveRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface LeaveRequestRepository extends MongoRepository<LeaveRequest, String> {
    List<LeaveRequest> findByClassNameIn(Set<String> classNames);
    List<LeaveRequest> findBySenderName(String senderName);

}

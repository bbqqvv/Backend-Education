package org.bbqqvv.backendeducation.service;

import org.bbqqvv.backendeducation.dto.request.LeaveRequestRequest;
import org.bbqqvv.backendeducation.dto.request.UpdateLeaveStatusRequest;
import org.bbqqvv.backendeducation.dto.response.LeaveRequestResponse;
import org.bbqqvv.backendeducation.entity.LeaveStatus;

import java.util.List;

public interface LeaveRequestService {
    LeaveRequestResponse createLeaveRequest(LeaveRequestRequest request);
    List<LeaveRequestResponse> getAllLeaveRequests();
    List<LeaveRequestResponse> getLeaveRequestsByCurrentTeacher();
    LeaveRequestResponse updateStatus(String id, UpdateLeaveStatusRequest request);

    void deleteLeaveRequest(String id);
}

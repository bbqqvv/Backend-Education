package org.bbqqvv.backendeducation.mapper;

import org.bbqqvv.backendeducation.dto.response.LeaveRequestResponse;
import org.bbqqvv.backendeducation.entity.LeaveRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LeaveRequestMapper {

    LeaveRequestResponse toLeaveRequestResponse(LeaveRequest leaveRequest);
}

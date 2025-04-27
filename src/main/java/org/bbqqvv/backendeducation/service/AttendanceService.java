package org.bbqqvv.backendeducation.service;

import jakarta.validation.Valid;
import org.bbqqvv.backendeducation.dto.request.AttendanceRequest;
import org.bbqqvv.backendeducation.dto.response.AttendanceResponse;
import java.util.List;

public interface AttendanceService {
    AttendanceResponse recordAttendance(@Valid AttendanceRequest req);
    List<AttendanceResponse> recordAttendances(@Valid List<AttendanceRequest> requests);
}

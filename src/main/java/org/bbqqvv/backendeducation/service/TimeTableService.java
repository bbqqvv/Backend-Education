package org.bbqqvv.backendeducation.service;

import org.bbqqvv.backendeducation.dto.request.TimeTableRequest;
import org.bbqqvv.backendeducation.dto.response.TimeTableResponse;

import java.util.List;

public interface TimeTableService {
    TimeTableResponse create(TimeTableRequest request);
    List<TimeTableResponse> getByClassName(String className);
    List<TimeTableResponse> getTimeTableForCurrentUser();
    void delete(String id);

    TimeTableResponse update(String id, TimeTableRequest request);
}
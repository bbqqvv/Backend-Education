package org.bbqqvv.backendeducation.service;

import org.bbqqvv.backendeducation.dto.request.ExamScheduleRequest;
import org.bbqqvv.backendeducation.dto.response.ExamScheduleResponse;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ExamScheduleService {
    ExamScheduleResponse create(ExamScheduleRequest request);
    ExamScheduleResponse update(String id, ExamScheduleRequest request);
    void delete(String id);

    List<ExamScheduleResponse> getMyExamSchedule();
    List<ExamScheduleResponse> getByClassName(String className);
}
package org.bbqqvv.backendeducation.service;

import jakarta.validation.Valid;
import org.bbqqvv.backendeducation.dto.PageResponse;
import org.bbqqvv.backendeducation.dto.request.ViolationRequest;
import org.bbqqvv.backendeducation.dto.response.ViolationResponse;
import org.springframework.data.domain.Pageable;

public interface ViolationService {
    ViolationResponse updateViolation(String id, @Valid ViolationRequest request);

    void deleteViolation(String id);

    PageResponse<ViolationResponse> getCurrentUserViolations(Pageable pageable);

    ViolationResponse getViolationById(String id);

    PageResponse<ViolationResponse> getAllViolations(Pageable pageable);

    ViolationResponse addViolation(@Valid ViolationRequest request);
}

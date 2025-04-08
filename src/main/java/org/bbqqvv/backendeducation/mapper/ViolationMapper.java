package org.bbqqvv.backendeducation.mapper;

import org.bbqqvv.backendeducation.dto.response.ViolationResponse;
import org.bbqqvv.backendeducation.entity.Violation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ViolationMapper {
    ViolationResponse toViolationResponse(Violation violation);
}

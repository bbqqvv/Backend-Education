package org.bbqqvv.backendeducation.mapper;

import org.bbqqvv.backendeducation.dto.response.AttendanceResponse;
import org.bbqqvv.backendeducation.entity.AttendanceRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {
    @Mapping(source = "status", target = "status")
    AttendanceResponse toResponse(AttendanceRecord attendanceRecord);
}

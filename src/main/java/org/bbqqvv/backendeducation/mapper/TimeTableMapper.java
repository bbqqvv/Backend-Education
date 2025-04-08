package org.bbqqvv.backendeducation.mapper;

import org.bbqqvv.backendeducation.dto.request.TimeTableRequest;
import org.bbqqvv.backendeducation.dto.response.TimeTableResponse;
import org.bbqqvv.backendeducation.entity.TimeTable;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TimeTableMapper {
    TimeTable toTimeTable(TimeTableRequest timeTableRequest);
    TimeTableResponse toTimeTableResponse(TimeTable timeTable);
}

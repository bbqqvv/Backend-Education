package org.bbqqvv.backendeducation.mapper;

import org.bbqqvv.backendeducation.dto.request.NewsletterRequest;
import org.bbqqvv.backendeducation.dto.response.NewsletterResponse;
import org.bbqqvv.backendeducation.entity.Newsletter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NewsletterMapper {

    // Khi trả response, MAPPING FULL thông tin từ entity → response
    NewsletterResponse toNewsletterResponse(Newsletter newsletter);
}

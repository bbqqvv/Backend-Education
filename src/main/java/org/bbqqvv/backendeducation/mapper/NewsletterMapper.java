package org.bbqqvv.backendeducation.mapper;
import org.bbqqvv.backendeducation.dto.response.NewsletterResponse;
import org.bbqqvv.backendeducation.entity.Newsletter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NewsletterMapper {
    NewsletterResponse toNewsletterResponse(Newsletter newsletter);
}

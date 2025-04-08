package org.bbqqvv.backendeducation.mapper;

import org.bbqqvv.backendeducation.dto.response.QuoteResponse;
import org.bbqqvv.backendeducation.entity.Quote;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuoteMapper {
    QuoteResponse toQuoteResponse(Quote quote);
}

package org.bbqqvv.backendeducation.service;

import jakarta.validation.Valid;
import org.bbqqvv.backendeducation.dto.request.QuoteRequest;
import org.bbqqvv.backendeducation.dto.response.QuoteResponse;

import java.util.List;

public interface QuoteService {
//    QuoteResponse addOrUpdateQuote(@Valid QuoteRequest request);

    List<QuoteResponse> addOrUpdateQuotes(List<@Valid QuoteRequest> requests);

    QuoteResponse getRandomQuote();
}

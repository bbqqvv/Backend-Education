package org.bbqqvv.backendeducation.service;

import jakarta.validation.Valid;
import org.bbqqvv.backendeducation.dto.request.QuoteRequest;
import org.bbqqvv.backendeducation.dto.response.QuoteResponse;

public interface QuoteService {
    QuoteResponse addOrUpdateQuote(@Valid QuoteRequest request);

    QuoteResponse getRandomQuote();
}

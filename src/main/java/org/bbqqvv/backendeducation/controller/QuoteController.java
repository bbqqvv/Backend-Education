package org.bbqqvv.backendeducation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bbqqvv.backendeducation.dto.ApiResponse;
import org.bbqqvv.backendeducation.dto.request.QuoteRequest;
import org.bbqqvv.backendeducation.dto.response.QuoteResponse;
import org.bbqqvv.backendeducation.service.QuoteService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quotes")
@RequiredArgsConstructor
public class QuoteController {

    private final QuoteService quoteService;

//    @PostMapping("/add-or-update")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ApiResponse<QuoteResponse> addOrUpdateQuote(
//            @Valid @RequestBody QuoteRequest request) {
//        QuoteResponse response = quoteService.addOrUpdateQuote(request);
//        return ApiResponse.<QuoteResponse>builder()
//                .success(true)
//                .message("Quote saved successfully")
//                .data(response)
//                .build();
//    }
    @PostMapping("/add-or-update")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> addOrUpdateQuotes(
            @Validated @RequestBody List<@Valid QuoteRequest> requests) {
        List<QuoteResponse> responses = quoteService.addOrUpdateQuotes(requests);

        return ApiResponse.<List<QuoteResponse>>builder()
                .success(true)
                .message("Quotes saved successfully")
                .data(responses)
                .build();
    }

    @GetMapping("/random")
    public ApiResponse<QuoteResponse> getRandomQuote() {
        QuoteResponse quote = quoteService.getRandomQuote();
        return ApiResponse.<QuoteResponse>builder()
                .success(true)
                .message("Random quote")
                .data(quote)
                .build();
    }
}

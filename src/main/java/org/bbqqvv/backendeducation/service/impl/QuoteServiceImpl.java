package org.bbqqvv.backendeducation.service.impl;

import lombok.RequiredArgsConstructor;
import org.bbqqvv.backendeducation.dto.request.QuoteRequest;
import org.bbqqvv.backendeducation.dto.response.QuoteResponse;
import org.bbqqvv.backendeducation.entity.Quote;
import org.bbqqvv.backendeducation.exception.AppException;
import org.bbqqvv.backendeducation.exception.ErrorCode;
import org.bbqqvv.backendeducation.mapper.QuoteMapper;
import org.bbqqvv.backendeducation.repository.QuoteRepository;
import org.bbqqvv.backendeducation.service.QuoteService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class QuoteServiceImpl implements QuoteService {

    private final QuoteRepository quoteRepository;
    private final QuoteMapper quoteMapper;

    @Override
    public QuoteResponse addOrUpdateQuote(QuoteRequest request) {
        Quote quote = Quote.builder()
                .content(request.getContent())
                .author(request.getAuthor())
                .createdAt(LocalDateTime.now())
                .build();

        return quoteMapper.toQuoteResponse(quoteRepository.save(quote));
    }

    @Override
    public QuoteResponse getRandomQuote() {
        long count = quoteRepository.count();
        if (count == 0) {
            throw new AppException(ErrorCode.QUOTE_NOT_FOUND);
        }

        int randomIndex = new Random().nextInt((int) count);
        Quote quote = quoteRepository.findAll(PageRequest.of(randomIndex, 1))
                .getContent()
                .get(0);

        return quoteMapper.toQuoteResponse(quote);
    }
}

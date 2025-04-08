package org.bbqqvv.backendeducation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bbqqvv.backendeducation.dto.ApiResponse;
import org.bbqqvv.backendeducation.dto.PageResponse;
import org.bbqqvv.backendeducation.dto.request.NewsletterRequest;
import org.bbqqvv.backendeducation.dto.response.NewsletterLikeResponse;
import org.bbqqvv.backendeducation.dto.response.NewsletterResponse;
import org.bbqqvv.backendeducation.service.NewsletterService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/newsletters")
@RequiredArgsConstructor
public class NewsletterController {

    private final NewsletterService newsletterService;

    /**
     * Thêm hoặc cập nhật bài viết newsletter
     */
    @PostMapping("/add-or-update")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<NewsletterResponse> addOrUpdateNewsletter(
            @ModelAttribute @Valid NewsletterRequest request) {
        NewsletterResponse response = newsletterService.addOrUpdateNewsletter(request);
        return ApiResponse.<NewsletterResponse>builder()
                .success(true)
                .data(response)
                .message("Newsletter saved successfully")
                .build();
    }

    /**
     * Lấy danh sách newsletter phân trang
     */
    @GetMapping
    public ApiResponse<PageResponse<NewsletterResponse>> getAllNewsletters(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

        PageResponse<NewsletterResponse> page = newsletterService.getAllNewsletters(pageable);
        return ApiResponse.<PageResponse<NewsletterResponse>>builder()
                .success(true)
                .message("List of newsletters")
                .data(page)
                .build();
    }

    /**
     * Lấy chi tiết một bài viết
     */
    @GetMapping("/{id}")
    public ApiResponse<NewsletterResponse> getNewsletterById(@PathVariable String id) {
        return ApiResponse.<NewsletterResponse>builder()
                .success(true)
                .message("Newsletter detail")
                .data(newsletterService.getNewsletterById(id))
                .build();
    }

    /**
     * Xoá một newsletter
     */
    @DeleteMapping("/remove/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> deleteNewsletter(@PathVariable String id) {
        newsletterService.deleteNewsletter(id);
        return ApiResponse.<String>builder()
                .success(true)
                .message("Deleted successfully")
                .data("Deleted: " + id)
                .build();
    }

    /**
     * Lấy bài viết theo danh mục
     */
    @GetMapping("/category/{category}")
    public ApiResponse<PageResponse<NewsletterResponse>> getByCategory(
            @PathVariable String category,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

        return ApiResponse.<PageResponse<NewsletterResponse>>builder()
                .success(true)
                .message("Filtered by category")
                .data(newsletterService.getByCategory(category, pageable))
                .build();
    }

    /**
     * Like bài viết
     */
    @PatchMapping("/{id}/like")
    public ApiResponse<NewsletterLikeResponse> likeNewsletter(@PathVariable String id) {
        return ApiResponse.<NewsletterLikeResponse>builder()
                .success(true)
                .message("Liked successfully")
                .data(newsletterService.likeNewsletter(id))
                .build();
    }

}

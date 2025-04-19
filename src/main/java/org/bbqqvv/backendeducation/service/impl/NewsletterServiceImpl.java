package org.bbqqvv.backendeducation.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.bbqqvv.backendeducation.config.jwt.SecurityUtils;
import org.bbqqvv.backendeducation.dto.PageResponse;
import org.bbqqvv.backendeducation.dto.request.NewsletterRequest;
import org.bbqqvv.backendeducation.dto.response.NewsletterLikeResponse;
import org.bbqqvv.backendeducation.dto.response.NewsletterResponse;
import org.bbqqvv.backendeducation.entity.Newsletter;
import org.bbqqvv.backendeducation.entity.NewsletterLike;
import org.bbqqvv.backendeducation.entity.User;
import org.bbqqvv.backendeducation.exception.AppException;
import org.bbqqvv.backendeducation.exception.ErrorCode;
import org.bbqqvv.backendeducation.mapper.NewsletterMapper;
import org.bbqqvv.backendeducation.repository.NewsletterLikeRepository;
import org.bbqqvv.backendeducation.repository.NewsletterRepository;
import org.bbqqvv.backendeducation.repository.UserRepository;
import org.bbqqvv.backendeducation.service.NewsletterService;
import org.bbqqvv.backendeducation.service.img.CloudinaryService;
import org.bbqqvv.backendeducation.util.PagingUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class NewsletterServiceImpl implements NewsletterService {

    private final NewsletterRepository newsletterRepository;
    private final NewsletterMapper newsletterMapper;
    private final CloudinaryService cloudinaryService;
    private final UserRepository userRepository;
    private final NewsletterLikeRepository newsletterLikeRepository;
    public NewsletterServiceImpl(NewsletterRepository newsletterRepository, NewsletterMapper newsletterMapper, CloudinaryService cloudinaryService, UserRepository userRepository, NewsletterLikeRepository newsletterLikeRepository) {
        this.newsletterRepository = newsletterRepository;
        this.newsletterMapper = newsletterMapper;
        this.cloudinaryService = cloudinaryService;
        this.userRepository = userRepository;
        this.newsletterLikeRepository = newsletterLikeRepository;
    }

    private User getAuthenticatedUser() {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
    @Override
    public NewsletterResponse addOrUpdateNewsletter(NewsletterRequest request) {
        try {
            long startTime = System.currentTimeMillis();

            // Upload thumbnail nếu có
            String thumbnailUrl = uploadIfPresent(request.getThumbnailUrl());

            // Upload ảnh content song song (nếu có)
            List<String> contentImages = request.getContentImages() != null
                    ? request.getContentImages().parallelStream()
                    .map(this::uploadIfPresent)
                    .toList()
                    : null;

            log.info("⏱ Uploaded images in {} ms", System.currentTimeMillis() - startTime);

            // Tạo đối tượng Newsletter
            Newsletter newsletter = Newsletter.builder()
                    .title(request.getTitle())
                    .content(request.getContent())
                    .excerpt(request.getExcerpt())
                    .tags(request.getTags())
                    .category(request.getCategory())
                    .thumbnailUrl(thumbnailUrl)
                    .contentImages(contentImages)
                    .author("System")
                    .viewCount(0)
                    .likeCount(0)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            // Lưu vào DB
            newsletter = newsletterRepository.save(newsletter);

            // Mapping thủ công những trường mapper không xử lý
            NewsletterResponse response = newsletterMapper.toNewsletterResponse(newsletter);
            response.setContentImages(contentImages);
            response.setThumbnailUrl(thumbnailUrl);

            return response;

        } catch (Exception e) {
            log.error("❌ Error while saving newsletter: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.IMAGE_UPLOAD_FAILED);
        }
    }


    private String uploadIfPresent(MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                return cloudinaryService.uploadImage(file);
            }
        } catch (Exception e) {
            log.error("Failed to upload image: {}", file.getOriginalFilename(), e);
            throw new AppException(ErrorCode.IMAGE_UPLOAD_FAILED);
        }
        return null;
    }

    @Override
    public PageResponse<NewsletterResponse> getAllNewsletters(Pageable pageable) {
        Page<Newsletter> page = newsletterRepository.findAll(pageable);
        return PagingUtil.toPageResponse(page, newsletterMapper::toNewsletterResponse);
    }

    @Override
    public NewsletterResponse getNewsletterById(String id) {
        Newsletter newsletter = newsletterRepository.findById(id)
                .map(n -> {
                    n.setViewCount(n.getViewCount() + 1);
                    n.setUpdatedAt(LocalDateTime.now());
                    return newsletterRepository.save(n);
                })
                .orElseThrow(() -> new AppException(ErrorCode.NEWSLETTER_NOT_FOUND));

        return newsletterMapper.toNewsletterResponse(newsletter);
    }

    @Override
    public void deleteNewsletter(String id) {
        if (!newsletterRepository.existsById(id)) {
            throw new AppException(ErrorCode.NEWSLETTER_NOT_FOUND);
        }
        newsletterRepository.deleteById(id);
    }

    @Override
    public PageResponse<NewsletterResponse> getByCategory(String category, Pageable pageable) {
        Page<Newsletter> page = newsletterRepository.findByCategoryIgnoreCase(category, pageable);
        return PagingUtil.toPageResponse(page, newsletterMapper::toNewsletterResponse);
    }

    @Override
    public NewsletterLikeResponse likeNewsletter(String newsletterId) {
        Newsletter newsletter = newsletterRepository.findById(newsletterId)
                .orElseThrow(() -> new AppException(ErrorCode.NEWSLETTER_NOT_FOUND));

        User user = getAuthenticatedUser(); // ✅ Dùng method tiện lợi

        NewsletterLike existingLike = newsletterLikeRepository
                .findByNewsletterIdAndUserId(newsletterId, user.getId())
                .orElse(null);

        boolean liked;
        if (existingLike != null) {
            newsletterLikeRepository.delete(existingLike);
            newsletter.setLikeCount(newsletter.getLikeCount() - 1);
            liked = false;
        } else {
            NewsletterLike newLike = NewsletterLike.builder()
                    .newsletterId(newsletterId)
                    .userId(user.getId())
                    .likedAt(LocalDateTime.now())
                    .build();
            newsletterLikeRepository.save(newLike);
            newsletter.setLikeCount(newsletter.getLikeCount() + 1);
            liked = true;
        }

        newsletter.setUpdatedAt(LocalDateTime.now());
        newsletterRepository.save(newsletter);

        return NewsletterLikeResponse.builder()
                .newsletterId(newsletterId)
                .userId(user.getId()) // or user.getUsername()
                .name(user.getFullName())
                .liked(liked)
                .totalLikes(newsletter.getLikeCount())
                .build();
    }
}
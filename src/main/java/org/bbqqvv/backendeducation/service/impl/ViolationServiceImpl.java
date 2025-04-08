package org.bbqqvv.backendeducation.service.impl;

import lombok.RequiredArgsConstructor;
import org.bbqqvv.backendeducation.config.jwt.SecurityUtils;
import org.bbqqvv.backendeducation.dto.PageResponse;
import org.bbqqvv.backendeducation.dto.request.ViolationRequest;
import org.bbqqvv.backendeducation.dto.response.ViolationResponse;
import org.bbqqvv.backendeducation.entity.User;
import org.bbqqvv.backendeducation.entity.UserType;
import org.bbqqvv.backendeducation.entity.Violation;
import org.bbqqvv.backendeducation.exception.AppException;
import org.bbqqvv.backendeducation.exception.ErrorCode;
import org.bbqqvv.backendeducation.mapper.ViolationMapper;
import org.bbqqvv.backendeducation.repository.UserRepository;
import org.bbqqvv.backendeducation.repository.ViolationRepository;
import org.bbqqvv.backendeducation.service.ViolationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.bbqqvv.backendeducation.util.PagingUtil.toPageResponse;

@Service
@RequiredArgsConstructor
public class ViolationServiceImpl implements ViolationService {

    private final ViolationRepository violationRepository;
    private final ViolationMapper violationMapper;
    private final UserRepository userRepository;

    /**
     * Lấy thông tin người dùng đang đăng nhập
     */
    private User getAuthenticatedUser() {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    /**
     * Tìm vi phạm theo ID
     */
    private Violation findViolationById(String id) {
        return violationRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.VIOLATION_NOT_FOUND));
    }

    /**
     * Chuyển đổi role từ User sang UserType enum
     */
    private UserType getUserTypeFromRoles(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> "ROLE_STUDENT".equals(role.getAuthority())) ? UserType.STUDENT : UserType.TEACHER;
    }

    /**
     * Tạo vi phạm mới
     */
    @Override
    @Transactional
    public ViolationResponse addViolation(ViolationRequest request) {
        User violatedUser = userRepository.findByStudentCode(request.getStudentCode())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Violation violation = Violation.builder()
                .studentCode(violatedUser.getStudentCode())
                .fullName(violatedUser.getFullName())
                .role(getUserTypeFromRoles(violatedUser))
                .description(request.getDescription())
                .level(request.getLevel())
                .createdAt(LocalDateTime.now())
                .createdBy(getAuthenticatedUser().getEmail())
                .build();

        Violation saved = violationRepository.save(violation);
        return violationMapper.toViolationResponse(saved);
    }

    /**
     * Cập nhật vi phạm
     */
    @Override
    @Transactional
    public ViolationResponse updateViolation(String id, ViolationRequest request) {
        User violatedUser = userRepository.findByStudentCode(request.getStudentCode())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Violation violation = findViolationById(id);

        violation.setStudentCode(violatedUser.getStudentCode());
        violation.setFullName(violatedUser.getFullName());
        violation.setRole(getUserTypeFromRoles(violatedUser));
        violation.setDescription(request.getDescription());
        violation.setLevel(request.getLevel());
        violation.setUpdatedAt(LocalDateTime.now());

        Violation updated = violationRepository.save(violation);
        return violationMapper.toViolationResponse(updated);
    }

    /**
     * Xoá vi phạm
     */
    @Override
    @Transactional
    public void deleteViolation(String id) {
        Violation violation = findViolationById(id);
        violationRepository.delete(violation);
    }

    /**
     * Lấy danh sách vi phạm của người dùng hiện tại
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<ViolationResponse> getCurrentUserViolations(Pageable pageable) {
        User currentUser = getAuthenticatedUser();
        Page<Violation> page = violationRepository.findByStudentCode(currentUser.getStudentCode(), pageable);
        return toPageResponse(page, violationMapper::toViolationResponse);
    }

    /**
     * Lấy chi tiết vi phạm theo ID
     */
    @Override
    @Transactional(readOnly = true)
    public ViolationResponse getViolationById(String id) {
        Violation violation = findViolationById(id);
        return violationMapper.toViolationResponse(violation);
    }

    /**
     * Lấy danh sách tất cả vi phạm
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<ViolationResponse> getAllViolations(Pageable pageable) {
        Page<Violation> page = violationRepository.findAll(pageable);
        return toPageResponse(page, violationMapper::toViolationResponse);
    }
}

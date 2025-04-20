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

    private User getAuthenticatedUser() {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private Violation findViolationById(String id) {
        return violationRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.VIOLATION_NOT_FOUND));
    }

    private UserType getUserTypeFromRoles(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> "ROLE_STUDENT".equals(role.getAuthority())) ? UserType.STUDENT : UserType.TEACHER;
    }
    private String generateViolationCode() {
        return "VL-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    @Transactional
    public ViolationResponse addViolation(ViolationRequest request) {
        User violatedUser = userRepository.findByUserCode(request.getUserCode())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Violation violation = Violation.builder()
                .violationCode(generateViolationCode()) // <= Gán mã tự sinh
                .userCode(violatedUser.getUserCode())
                .fullName(violatedUser.getFullName())
                .role(getUserTypeFromRoles(violatedUser))
                .description(request.getDescription())
                .level(request.getLevel())
                .createdAt(LocalDateTime.now())
                .createdBy(getAuthenticatedUser().getEmail())
                .build();

        return violationMapper.toViolationResponse(violationRepository.save(violation));
    }

    @Override
    @Transactional
    public ViolationResponse updateViolation(String id, ViolationRequest request) {
        Violation violation = findViolationById(id);

        User violatedUser = userRepository.findByUserCode(request.getUserCode())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        violation.setUserCode(violatedUser.getUserCode());
        violation.setFullName(violatedUser.getFullName());
        violation.setRole(getUserTypeFromRoles(violatedUser));
        violation.setDescription(request.getDescription());
        violation.setLevel(request.getLevel());
        violation.setUpdatedAt(LocalDateTime.now());

        return violationMapper.toViolationResponse(violationRepository.save(violation));
    }

    @Override
    @Transactional
    public void deleteViolation(String id) {
        violationRepository.delete(findViolationById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ViolationResponse> getCurrentUserViolations(Pageable pageable) {
        String userCode = getAuthenticatedUser().getUserCode();
        Page<Violation> page = violationRepository.findByUserCode(userCode, pageable);
        return toPageResponse(page, violationMapper::toViolationResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ViolationResponse getViolationById(String id) {
        return violationMapper.toViolationResponse(findViolationById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ViolationResponse> getAllViolations(Pageable pageable) {
        return toPageResponse(violationRepository.findAll(pageable), violationMapper::toViolationResponse);
    }
}

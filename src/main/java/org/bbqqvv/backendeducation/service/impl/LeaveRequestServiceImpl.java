package org.bbqqvv.backendeducation.service.impl;

import lombok.RequiredArgsConstructor;
import org.bbqqvv.backendeducation.config.jwt.SecurityUtils;
import org.bbqqvv.backendeducation.dto.request.LeaveRequestRequest;
import org.bbqqvv.backendeducation.dto.request.UpdateLeaveStatusRequest;
import org.bbqqvv.backendeducation.dto.response.LeaveRequestResponse;
import org.bbqqvv.backendeducation.entity.*;
import org.bbqqvv.backendeducation.exception.AppException;
import org.bbqqvv.backendeducation.exception.ErrorCode;
import org.bbqqvv.backendeducation.mapper.LeaveRequestMapper;
import org.bbqqvv.backendeducation.repository.LeaveRequestRepository;
import org.bbqqvv.backendeducation.repository.UserRepository;
import org.bbqqvv.backendeducation.service.EmailService;
import org.bbqqvv.backendeducation.service.LeaveRequestService;
import org.bbqqvv.backendeducation.service.img.CloudinaryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private static final String TEACHER_ROLE = "ROLE_TEACHER";
    private static final String STUDENT_ROLE = "ROLE_STUDENT";

    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveRequestMapper leaveRequestMapper;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private final EmailService emailService;

    @Override
    @Transactional
    public LeaveRequestResponse createLeaveRequest(LeaveRequestRequest request) {
        User requester = getAuthenticatedUser();
        validateLeaveRequest(request, requester);

        String className = determineClassName(requester, request);
        String recipient = determineRecipient(requester, className);
        String imageUrl = uploadImageIfPresent(request);

        LeaveRequest leaveRequest = LeaveRequest.builder()
                .senderName(requester.getFullName())
                .recipient(recipient)
                .reason(request.getReason())
                .className(className)
                .fromDate(request.getFromDate())
                .toDate(request.getToDate())
                .imageFile(imageUrl)
                .status(LeaveStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .build();

        leaveRequestRepository.save(leaveRequest);
        notifyRelevantParties(leaveRequest, requester);

        return leaveRequestMapper.toLeaveRequestResponse(leaveRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeaveRequestResponse> getAllLeaveRequests() {
        return leaveRequestRepository.findAll().stream()
                .map(leaveRequestMapper::toLeaveRequestResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeaveRequestResponse> getLeaveRequestsByCurrentUser() {
        User user = getAuthenticatedUser();
        List<LeaveRequest> leaveRequests;

        if (hasRole(user, TEACHER_ROLE)) {
            leaveRequests = leaveRequestRepository.findByClassNameIn(user.getTeachingClasses());
        } else {
            leaveRequests = leaveRequestRepository.findBySenderName(user.getFullName());
        }

        return leaveRequests.stream()
                .map(leaveRequestMapper::toLeaveRequestResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LeaveRequestResponse updateStatus(String id, UpdateLeaveStatusRequest request) {
        LeaveRequest leaveRequest = findLeaveRequestOrThrow(id);
        validateStatusUpdate(request, leaveRequest);

        leaveRequest.setStatus(request.getStatus());
        leaveRequest.setUpdatedAt(LocalDateTime.now());

        if (request.getStatus() == LeaveStatus.REJECTED) {
            leaveRequest.setRejectionReason(request.getRejectionReason());
        }

        leaveRequestRepository.save(leaveRequest);

        if (leaveRequest.getStatus() == LeaveStatus.APPROVED) {
            notifyClassMembers(leaveRequest);
        }

        return leaveRequestMapper.toLeaveRequestResponse(leaveRequest);
    }

    @Override
    @Transactional
    public void deleteLeaveRequest(String id) {
        LeaveRequest leaveRequest = findLeaveRequestOrThrow(id);
        User currentUser = getAuthenticatedUser();

        if (!leaveRequest.getSenderName().equals(currentUser.getFullName())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new AppException(ErrorCode.LEAVE_CANNOT_BE_DELETED);
        }

        leaveRequestRepository.delete(leaveRequest);
    }

    // ========== Helper methods ==========

    private User getAuthenticatedUser() {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private void validateLeaveRequest(LeaveRequestRequest request, User user) {
        if (request.getFromDate().isAfter(request.getToDate())) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }

        if (!hasRole(user, TEACHER_ROLE) && !hasRole(user, STUDENT_ROLE)) {
            throw new AppException(ErrorCode.UNAUTHORIZED_ACTION);
        }
    }

    private boolean hasRole(User user, String roleName) {
        return user.getRoles().stream().anyMatch(role -> role.name().equals(roleName));
    }

    private String determineClassName(User user, LeaveRequestRequest request) {
        if (hasRole(user, TEACHER_ROLE)) {
            if (request.getClassName() == null || !user.getTeachingClasses().contains(request.getClassName())) {
                throw new AppException(ErrorCode.INVALID_CLASS);
            }
            return request.getClassName();
        } else {
            return user.getStudentClass();
        }
    }

    private String determineRecipient(User requester, String className) {
        if (hasRole(requester, TEACHER_ROLE)) {
            return "Students of class " + className;
        } else {
            return "Homeroom teacher of class " + className;
        }
    }

    private String uploadImageIfPresent(LeaveRequestRequest request) {
        if (request.getImageFile() != null && !request.getImageFile().isEmpty()) {
            return cloudinaryService.uploadImage(request.getImageFile());
        }
        return null;
    }

    private void notifyRelevantParties(LeaveRequest leaveRequest, User requester) {
        Map<String, Object> model = buildEmailModel(leaveRequest);

        if (hasRole(requester, TEACHER_ROLE)) {
            userRepository.findByRolesAndStudentClass(Role.ROLE_STUDENT, leaveRequest.getClassName())
                    .forEach(student -> emailService.sendLeaveRequestEmail(student.getEmail(), model));
        } else {
            userRepository.findByRolesAndTeachingClasses(Role.ROLE_TEACHER, leaveRequest.getClassName())
                    .ifPresent(teacher -> emailService.sendLeaveRequestEmail(teacher.getEmail(), model));
        }
    }

    private void notifyClassMembers(LeaveRequest leaveRequest) {
        Map<String, Object> model = buildEmailModel(leaveRequest);
        userRepository.findByRolesAndStudentClass(Role.ROLE_STUDENT, leaveRequest.getClassName())
                .forEach(student -> emailService.sendLeaveRequestEmail(student.getEmail(), model));
    }

    private LeaveRequest findLeaveRequestOrThrow(String id) {
        return leaveRequestRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.LEAVE_NOT_FOUND));
    }

    private void validateStatusUpdate(UpdateLeaveStatusRequest request, LeaveRequest leaveRequest) {
        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new AppException(ErrorCode.LEAVE_ALREADY_PROCESSED);
        }

        if (request.getStatus() == LeaveStatus.REJECTED &&
                (request.getRejectionReason() == null || request.getRejectionReason().isBlank())) {
            throw new AppException(ErrorCode.REJECTION_REASON_REQUIRED);
        }
    }

    private Map<String, Object> buildEmailModel(LeaveRequest leaveRequest) {
        Map<String, Object> model = new HashMap<>();
        model.put("className", leaveRequest.getClassName());
        model.put("senderName", leaveRequest.getSenderName());
        model.put("fromDate", leaveRequest.getFromDate());
        model.put("toDate", leaveRequest.getToDate());
        model.put("reason", leaveRequest.getReason());
        model.put("status", leaveRequest.getStatus());
        model.put("imageFile", leaveRequest.getImageFile());

        if (leaveRequest.getStatus() == LeaveStatus.REJECTED) {
            model.put("rejectionReason", leaveRequest.getRejectionReason());
        }

        return model;
    }
}

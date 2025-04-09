package org.bbqqvv.backendeducation.service.impl;

import lombok.RequiredArgsConstructor;
import org.bbqqvv.backendeducation.config.jwt.SecurityUtils;
import org.bbqqvv.backendeducation.dto.request.LeaveRequestRequest;
import org.bbqqvv.backendeducation.dto.request.UpdateLeaveStatusRequest;
import org.bbqqvv.backendeducation.dto.response.LeaveRequestResponse;
import org.bbqqvv.backendeducation.entity.LeaveRequest;
import org.bbqqvv.backendeducation.entity.LeaveStatus;
import org.bbqqvv.backendeducation.entity.Role;
import org.bbqqvv.backendeducation.entity.User;
import org.bbqqvv.backendeducation.exception.AppException;
import org.bbqqvv.backendeducation.exception.ErrorCode;
import org.bbqqvv.backendeducation.mapper.LeaveRequestMapper;
import org.bbqqvv.backendeducation.repository.LeaveRequestRepository;
import org.bbqqvv.backendeducation.repository.UserRepository;
import org.bbqqvv.backendeducation.service.EmailService;
import org.bbqqvv.backendeducation.service.LeaveRequestService;
import org.bbqqvv.backendeducation.service.img.CloudinaryService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveRequestMapper leaveRequestMapper;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private final EmailService emailService;

    private User getAuthenticatedUser() {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public LeaveRequestResponse createLeaveRequest(LeaveRequestRequest request) {
        // Upload ảnh lên Cloudinary nếu có
        String imageUrl = request.getImageFile() != null && !request.getImageFile().isEmpty()
                ? cloudinaryService.uploadImage(request.getImageFile())
                : null;

        LeaveRequest leaveRequest = LeaveRequest.builder()
                .senderName(request.getSenderName())
                .recipient(request.getRecipient())
                .reason(request.getReason())
                .className(request.getClassName())
                .fromDate(request.getFromDate())
                .toDate(request.getToDate())
                .imageFile(imageUrl)
                .status(LeaveStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .build();

        leaveRequestRepository.save(leaveRequest);
        return leaveRequestMapper.toLeaveRequestResponse(leaveRequest);
    }

    @Override
    public List<LeaveRequestResponse> getAllLeaveRequests() {
        return leaveRequestRepository.findAll().stream()
                .map(leaveRequestMapper::toLeaveRequestResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<LeaveRequestResponse> getLeaveRequestsByCurrentTeacher() {
        User teacher = getAuthenticatedUser();

        List<LeaveRequest> leaveRequests = leaveRequestRepository.findBySenderName(teacher.getFullName());

        return leaveRequests.stream()
                .map(leaveRequestMapper::toLeaveRequestResponse)
                .collect(Collectors.toList());
    }


    @Override
    public LeaveRequestResponse updateStatus(String id, UpdateLeaveStatusRequest request) {
        LeaveRequest leave = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.LEAVE_NOT_FOUND));

        if (request.getStatus() == LeaveStatus.APPROVED) {
            leave.setStatus(LeaveStatus.APPROVED);
            leave.setUpdatedAt(LocalDateTime.now());

            List<User> students = userRepository.findByRolesAndStudentClass(Role.ROLE_STUDENT, leave.getClassName());
            for (User student : students) {
                Map<String, Object> model = buildEmailModel(leave);
                emailService.sendLeaveRequestEmail(student.getEmail(), model);
            }

        } else if (request.getStatus() == LeaveStatus.REJECTED) {
            if (request.getRejectionReason() == null || request.getRejectionReason().isBlank()) {
                throw new AppException(ErrorCode.INVALID_INPUT);
            }
            leave.setStatus(LeaveStatus.REJECTED);
            leave.setRejectionReason(request.getRejectionReason());
            leave.setUpdatedAt(LocalDateTime.now());
        }
        leaveRequestRepository.save(leave);
        return leaveRequestMapper.toLeaveRequestResponse(leave);
    }

    @Override
    public void deleteLeaveRequest(String id) {
        LeaveRequest leave = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.LEAVE_NOT_FOUND));

        User current = getAuthenticatedUser();

        if (!leave.getSenderName().equals(current.getFullName())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new AppException(ErrorCode.LEAVE_CANNOT_BE_DELETED);
        }

        leaveRequestRepository.delete(leave);
    }


    private Map<String, Object> buildEmailModel(LeaveRequest leave) {
        Map<String, Object> model = new HashMap<>();
        model.put("className", leave.getClassName());
        model.put("teacherName", leave.getSenderName());
        model.put("fromDate", leave.getFromDate());
        model.put("toDate", leave.getToDate());
        model.put("reason", leave.getReason());
        return model;
    }
}

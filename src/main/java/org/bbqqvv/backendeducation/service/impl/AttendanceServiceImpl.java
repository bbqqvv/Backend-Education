package org.bbqqvv.backendeducation.service.impl;

import lombok.RequiredArgsConstructor;
import org.bbqqvv.backendeducation.dto.request.AttendanceRequest;
import org.bbqqvv.backendeducation.dto.response.AttendanceResponse;
import org.bbqqvv.backendeducation.entity.AttendanceRecord;
import org.bbqqvv.backendeducation.entity.User;
import org.bbqqvv.backendeducation.mapper.AttendanceMapper;
import org.bbqqvv.backendeducation.repository.AttendanceRepository;
import org.bbqqvv.backendeducation.repository.UserRepository;
import org.bbqqvv.backendeducation.service.AttendanceService;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final AttendanceMapper attendanceMapper;

    @Override
    public AttendanceResponse recordAttendance(AttendanceRequest req) {
        // Lấy thông tin học sinh từ userCode
        User user = userRepository.findByUserCode(req.getUserCode())
                .orElseThrow(() -> new NotFoundException("User not found with code: " + req.getUserCode()));

        // Tạo đối tượng AttendanceRecord mà không cần userCode trong đây
        AttendanceRecord attendance = AttendanceRecord.builder()
                .studentId(user.getId())
                .studentName(user.getFullName())
                .className(user.getStudentClass())
                .status(req.getStatus())
                .date(req.getDate())
                .createdDate(LocalDateTime.now())
                .build();

        // Lưu AttendanceRecord vào MongoDB
        AttendanceRecord saved = attendanceRepository.save(attendance);

        // Chuyển đổi entity thành response
        return attendanceMapper.toResponse(saved);
    }

    @Override
    public List<AttendanceResponse> recordAttendances(List<AttendanceRequest> requests) {
        List<AttendanceRecord> records = requests.stream().map(req -> {
            // Tìm user thông qua userCode
            User user = userRepository.findByUserCode(req.getUserCode())
                    .orElseThrow(() -> new NotFoundException("User not found with code: " + req.getUserCode()));

            // Tạo AttendanceRecord mà không cần userCode trong entity
            return AttendanceRecord.builder()
                    .studentId(user.getId())
                    .studentName(user.getFullName())
                    .className(user.getStudentClass())
                    .status(req.getStatus())
                    .date(req.getDate())
                    .createdDate(LocalDateTime.now())
                    .build();
        }).collect(Collectors.toList());

        // Lưu danh sách AttendanceRecord vào MongoDB
        return attendanceRepository.saveAll(records).stream()
                .map(attendanceMapper::toResponse)
                .collect(Collectors.toList());
    }
}

package org.bbqqvv.backendeducation.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.bbqqvv.backendeducation.dto.request.ChangePasswordRequest;
import org.bbqqvv.backendeducation.dto.request.UpdateProfileRequest;
import org.bbqqvv.backendeducation.dto.request.UserCreationRequest;
import org.bbqqvv.backendeducation.dto.response.UserResponse;
import org.bbqqvv.backendeducation.entity.Role;
import org.bbqqvv.backendeducation.entity.User;
import org.bbqqvv.backendeducation.entity.UserProfile;
import org.bbqqvv.backendeducation.exception.AppException;
import org.bbqqvv.backendeducation.exception.ErrorCode;
import org.bbqqvv.backendeducation.mapper.UserMapper;
import org.bbqqvv.backendeducation.repository.UserRepository;
import org.bbqqvv.backendeducation.repository.UserProfileRepository;
import org.bbqqvv.backendeducation.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    UserProfileRepository userProfileRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        log.info("===> Request body: {}", request); // cần toString trong UserCreationRequest
        User user = userMapper.toUser(request);
        log.info("===> Mapped user: {}", user); // cần toString trong User
        user.onCreate();
        User savedUser = userRepository.save(user);

        UserProfile profile = UserProfile.builder()
                .userId(savedUser.getId())
                .build();
        userProfileRepository.save(profile);

        return userMapper.toUserResponse(savedUser, profile);
    }



    @Override
    public UserResponse changePassword(String email, ChangePasswordRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        UserProfile profile = userProfileRepository.findByUserId(user.getId())
                .orElse(null);

        return userMapper.toUserResponse(user, profile);
    }

    @Override
    public UserResponse updateUserProfile(String email, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        UserProfile existingProfile = userProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_PROFILE_NOT_FOUND));

        if (request.getAddress() != null) existingProfile.setAddress(request.getAddress());
        if (request.getPhoneNumber() != null) existingProfile.setPhoneNumber(request.getPhoneNumber());
        if (request.getDateOfBirth() != null) existingProfile.setDateOfBirth(request.getDateOfBirth());
        if (request.getGender() != null) existingProfile.setGender(request.getGender());
        if (request.getFatherName() != null) existingProfile.setFatherName(request.getFatherName());
        if (request.getMotherName() != null) existingProfile.setMotherName(request.getMotherName());
        if (request.getFatherPhoneNumber() != null) existingProfile.setFatherPhoneNumber(request.getFatherPhoneNumber());
        if (request.getMotherPhoneNumber() != null) existingProfile.setMotherPhoneNumber(request.getMotherPhoneNumber());
        userProfileRepository.save(existingProfile);

        return userMapper.toUserResponse(user, existingProfile);
    }

    @Override
    public User getUserByEmailEntity(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public List<UserResponse> getClassmates(String email) {
        User user = getUserByEmailEntity(email);

        return userRepository.findByStudentClass(user.getStudentClass()).stream()
                .filter(u -> !u.getEmail().equals(email))
                .map(u -> {
                    UserProfile profile = userProfileRepository.findByUserId(u.getId()).orElse(null);
                    return userMapper.toUserResponse(u, profile);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> getTeachersForClass(String className) {
        List<User> teachers = userRepository.findAll().stream()
                .filter(user -> user.getRoles() != null && user.getRoles().contains(Role.ROLE_TEACHER))
                .filter(user -> className.equals(user.getStudentClass()))
                .toList();

        return teachers.stream()
                .map(teacher -> {
                    UserProfile profile = userProfileRepository.findByUserId(teacher.getId()).orElse(null);
                    return userMapper.toUserResponse(teacher, profile);
                })
                .collect(Collectors.toList());
    }



    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(u -> {
                    UserProfile profile = userProfileRepository.findByUserId(u.getId()).orElse(null);
                    return userMapper.toUserResponse(u, profile);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Set<String> getClassesTaughtByTeacher(String teacherEmail) {
        User teacher = getUserByEmailEntity(teacherEmail);

        if (teacher.getRoles() == null || !teacher.getRoles().contains(Role.ROLE_TEACHER)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        Set<String> teachingClasses = teacher.getTeachingClasses(); // dùng Set
        if (teachingClasses == null || teachingClasses.isEmpty()) {
            return Set.of(); // Không dạy lớp nào
        }

        return teachingClasses;
    }

    @Override
    public List<UserResponse> getStudentsForTeacherClass(String teacherEmail, String className) {
        User teacher = getUserByEmailEntity(teacherEmail);

        if (teacher.getRoles() == null || !teacher.getRoles().contains(Role.ROLE_TEACHER)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        Set<String> teachingClasses = teacher.getTeachingClasses(); // dùng Set
        if (teachingClasses == null || !teachingClasses.contains(className)) {
            throw new AppException(ErrorCode.FORBIDDEN); // Không dạy lớp này
        }

        return userRepository.findByStudentClass(className).stream()
                .filter(user -> user.getRoles() != null && user.getRoles().contains(Role.ROLE_STUDENT))
                .map(student -> {
                    UserProfile profile = userProfileRepository.findByUserId(student.getId()).orElse(null);
                    return userMapper.toUserResponse(student, profile);
                })
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        UserProfile profile = userProfileRepository.findByUserId(user.getId())
                .orElse(null);

        return userMapper.toUserResponse(user, profile);
    }


}

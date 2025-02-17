package org.bbqqvv.backendeducation.service.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.bbqqvv.backendeducation.dto.request.ChangePasswordRequest;
import org.bbqqvv.backendeducation.dto.request.UserCreationRequest;
import org.bbqqvv.backendeducation.dto.response.UserResponse;
import org.bbqqvv.backendeducation.entity.User;
import org.bbqqvv.backendeducation.exception.AppException;
import org.bbqqvv.backendeducation.exception.ErrorCode;
import org.bbqqvv.backendeducation.mapper.UserMapper;
import org.bbqqvv.backendeducation.repository.UserRepository;
import org.bbqqvv.backendeducation.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        User user = userMapper.toUser(request);
        user.onCreate();
        User savedUser = userRepository.save(user);
        return userMapper.toUserResponse(savedUser);
    }

    @Override
    public UserResponse getUserById(String id) {
        // Kiểm tra người dùng có tồn tại không
        User user = userRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND)
        );
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse changePassword(String email, ChangePasswordRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        }

        // Mã hóa mật khẩu mới và cập nhật
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

    @Override
    public User getUserByEmailEntity(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse updateUser(String id, UserCreationRequest request) {
        // Kiểm tra người dùng có tồn tại không
        User user = userRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND)
        );

        // Cập nhật thông tin từ DTO
        user.setFullName(request.getFullName());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());

        // Nếu cần, có thể cập nhật roles ở đây
        // Ví dụ: user.setRoles(java.util.Set.of(Role.valueOf(request.getRole().toUpperCase())));

        // Lưu lại
        User updatedUser = userRepository.save(user);

        // Map User entity -> UserResponse
        return userMapper.toUserResponse(updatedUser);
    }


    @Override
    public void deleteUser(String id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND)
        );
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}

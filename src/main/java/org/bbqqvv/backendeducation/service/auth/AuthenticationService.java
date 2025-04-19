package org.bbqqvv.backendeducation.service.auth;

import org.bbqqvv.backendeducation.config.jwt.JwtTokenUtil;
import org.bbqqvv.backendeducation.dto.request.AuthenticationRequest;
import org.bbqqvv.backendeducation.dto.request.UserCreationRequest;
import org.bbqqvv.backendeducation.dto.response.UserResponse;
import org.bbqqvv.backendeducation.service.UserService;
import org.bbqqvv.backendeducation.util.ValidateUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthenticationService(UserService userService,
                                 PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager,
                                 JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    // Đăng ký người dùng mới
    public UserResponse register(UserCreationRequest registerUserDto) {
        // Kiểm tra email hợp lệ
        ValidateUtils.validateEmail(registerUserDto.getEmail());

        // Kiểm tra nếu email đã tồn tại
        if (userService.existsByEmail(registerUserDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Tạo mật khẩu ngẫu nhiên và mã hóa nó
        String rawPassword = generateRandomPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Tạo đối tượng UserCreationRequest mới với mật khẩu đã mã hóa
        UserCreationRequest userRequest = UserCreationRequest.builder()
                .fullName(registerUserDto.getFullName())
                .email(registerUserDto.getEmail())
                .password(encodedPassword)
                .userCode(registerUserDto.getUserCode())
                .studentClass(registerUserDto.getStudentClass())
                .teachingClasses(registerUserDto.getTeachingClasses())
                .role(registerUserDto.getRole())
                .build();

        // Tạo user và lưu vào cơ sở dữ liệu
        UserResponse userResponse = userService.createUser(userRequest);

        // Trả lại mật khẩu gốc để gửi qua email cho người dùng
        userResponse.setPassword(rawPassword);

        return userResponse;
    }

    // Đăng nhập
    public String login(AuthenticationRequest loginUserDto) {
        // Xác thực người dùng
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword())
        );

        org.springframework.security.core.userdetails.User userDetails =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        // Tạo token chứa roles
        return jwtTokenUtil.generateToken(userDetails.getUsername(), userDetails.getAuthorities());
    }

    // ===============================
    // 🚀 Private method: Tạo mật khẩu ngẫu nhiên
    // ===============================
    private String generateRandomPassword() {
        return UUID.randomUUID().toString().substring(0, 8); // Tạo mật khẩu ngẫu nhiên dài 8 ký tự
    }
}

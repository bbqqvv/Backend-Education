package org.bbqqvv.backendeducation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bbqqvv.backendeducation.dto.ApiResponse;
import org.bbqqvv.backendeducation.dto.request.*;
import org.bbqqvv.backendeducation.dto.response.*;
import org.bbqqvv.backendeducation.service.OtpService;
import org.bbqqvv.backendeducation.service.auth.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final OtpService otpService;

    // Đăng ký người dùng
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserCreationRequest creationRequest) {
        try {
            UserResponse userResponse = authenticationService.register(creationRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
        } catch (Exception e) {
            log.error("Registration failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
        }
    }

    // Đăng nhập người dùng
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        try {
            String token = authenticationService.login(authenticationRequest);
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (BadCredentialsException e) {
            log.warn("Invalid login attempt: {}", authenticationRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception e) {
            log.error("Login error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login failed: " + e.getMessage());
        }
    }


    /**
     * Gửi OTP để đặt lại mật khẩu
     */
    @PostMapping("/forgot-password")
    public ApiResponse<OtpResponse> forgotPassword(@RequestBody @Valid OtpRequest request) {
        String message = otpService.sendOtp(request.getEmail());
        return ApiResponse.<OtpResponse>builder()
                .success(true)
                .message("OTP sent successfully")
                .data(new OtpResponse(message, request.getEmail()))
                .build();
    }

    /**
     * Xác thực OTP
     */
    @PostMapping("/verify-otp")
    public ApiResponse<OtpVerificationResponse> verifyOtp(@RequestBody @Valid OtpVerificationRequest request) {
        String result = otpService.verifyOtp(request.getEmail(), request.getOtp());
        boolean success = result.equals("OTP verified successfully!");

        return ApiResponse.<OtpVerificationResponse>builder()
                .success(success)
                .message(result)
                .data(new OtpVerificationResponse(result, success))
                .build();
    }

    /**
     * Đặt lại mật khẩu sau khi xác thực OTP thành công
     */
    @PostMapping("/reset-password")
    public ApiResponse<ResetPasswordResponse> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        String result = otpService.resetPassword(request.getEmail(), request.getNewPassword());
        boolean success = result.equals("Password reset successful!");

        return ApiResponse.<ResetPasswordResponse>builder()
                .success(success)
                .message(result)
                .data(new ResetPasswordResponse(result, success))
                .build();
    }
}

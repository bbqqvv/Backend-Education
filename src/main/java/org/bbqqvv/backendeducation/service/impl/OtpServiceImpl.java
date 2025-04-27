package org.bbqqvv.backendeducation.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.bbqqvv.backendeducation.entity.Otp;
import org.bbqqvv.backendeducation.entity.User;
import org.bbqqvv.backendeducation.repository.OtpRepository;
import org.bbqqvv.backendeducation.repository.UserRepository;
import org.bbqqvv.backendeducation.service.OtpService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
public class OtpServiceImpl implements OtpService {

    private final OtpRepository otpRepository;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final SpringTemplateEngine templateEngine;

    private static final int OTP_EXPIRATION_MINUTES = 5;

    public OtpServiceImpl(OtpRepository otpRepository,
                          UserRepository userRepository,
                          JavaMailSender mailSender,
                          PasswordEncoder passwordEncoder,
                          SpringTemplateEngine templateEngine) {
        this.otpRepository = otpRepository;
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
        this.templateEngine = templateEngine;
    }

    /**
     * Gửi OTP đến email
     */
    @Override
    @Transactional
    public String sendOtp(String email) {
        otpRepository.findByEmail(email).ifPresent(otpRepository::delete);

        String otp = generateOtp();
        Otp otpEntity = new Otp(email, otp, LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTES));
        otpRepository.save(otpEntity);

        try {
            sendOtpEmail(email, otp);
            return "OTP sent successfully!";
        } catch (MessagingException e) {
            log.error("Error sending OTP email: {}", e.getMessage());
            return "Failed to send OTP!";
        }
    }

    /**
     * Xác thực OTP
     */
    @Override
    @Transactional
    public String verifyOtp(String email, String otp) {
        Optional<Otp> otpEntityOptional = otpRepository.findByEmail(email);

        if (otpEntityOptional.isEmpty()) return "OTP not found!";
        Otp otpEntity = otpEntityOptional.get();

        if (!otpEntity.getOtp().equals(otp)) return "Invalid OTP!";
        if (otpEntity.getExpiryTime().isBefore(LocalDateTime.now())) return "OTP expired!";

        otpRepository.delete(otpEntity);
        return "OTP verified successfully!";
    }

    /**
     * Đặt lại mật khẩu sau khi xác thực OTP thành công
     */
    @Override
    @Transactional
    public String resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return "Password reset successful!";
    }

    /**
     * Tạo mã OTP ngẫu nhiên gồm 6 chữ số
     */
    private String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    /**
     * Gửi email chứa mã OTP bằng template Thymeleaf
     */
    private void sendOtpEmail(String email, String otp) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject("Mã xác thực OTP của bạn");

        Context context = new Context();
        context.setVariable("otp", otp);
        context.setVariable("expiration", OTP_EXPIRATION_MINUTES);

        String htmlContent = templateEngine.process("otp-email.html", context);
        helper.setText(htmlContent, true);

        helper.addInline("logo", new ClassPathResource("static/images/logo-app.jpg"));

        mailSender.send(message);
    }
}

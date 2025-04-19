package org.bbqqvv.backendeducation.config;

import org.bbqqvv.backendeducation.config.jwt.JwtAuthenticationFilter;
import org.bbqqvv.backendeducation.config.jwt.JwtTokenUtil;
import org.bbqqvv.backendeducation.service.auth.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity// Đảm bảo có dòng này thì @PreAuthorize("hasRole('ADMIN')") ở controller mới hoạt động

public class SecurityConfig {

	private final CustomUserDetailsService customUserDetailsService;
	private final JwtTokenUtil jwtTokenUtil;

	public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtTokenUtil jwtTokenUtil) {
		this.customUserDetailsService = customUserDetailsService;
		this.jwtTokenUtil = jwtTokenUtil;
	}

	// Các URL không yêu cầu xác thực
	private static final String[] WHITE_LIST_URL = {
			"/api/auth/**",
			"/api/newsletters/**",
			"/api/quotes/**",
			"/api/leave-requests/**",
			"/api/violations/**",
			"/api/chatbot/**"


	};
	private static final String[] SECURED_URL_PATTERNS = {
			"/api/**",
			"/admin/**",
			"/teacher/**",
			"/student/**"
	};

	// Cấu hình SecurityFilterChain
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.cors(cors -> cors.configurationSource(corsConfigurationSource())) // Cấu hình CORS
				.csrf(AbstractHttpConfigurer::disable) // Tắt CSRF cho API REST
				.authorizeHttpRequests(authz -> authz
						.requestMatchers(WHITE_LIST_URL).permitAll() // Các yêu cầu GET không cần xác thực
						.requestMatchers(SECURED_URL_PATTERNS).authenticated() // Các phương thức POST, PUT, DELETE yêu cầu xác thực
				)
				.addFilterBefore(new JwtAuthenticationFilter(jwtTokenUtil, customUserDetailsService),
						UsernamePasswordAuthenticationFilter.class); // Thêm filter JWT vào chuỗi bảo mật

		return http.build();
	}

	// Cấu hình AuthenticationManager
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder =
				http.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(customUserDetailsService)
				.passwordEncoder(passwordEncoder());
		return authenticationManagerBuilder.build();
	}

	// Cấu hình PasswordEncoder
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// Cấu hình CORS
	private UrlBasedCorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of("http://localhost:8081", "http://192.168.1.4:8081")); // Thêm IP Expo
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		config.addAllowedHeader("*");
		config.setAllowCredentials(true);
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}

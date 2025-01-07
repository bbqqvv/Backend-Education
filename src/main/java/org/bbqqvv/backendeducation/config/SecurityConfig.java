package org.bbqqvv.backendeducation.config;


import org.bbqqvv.backendeducation.config.jwt.JwtAuthenticationFilter;
import org.bbqqvv.backendeducation.config.jwt.JwtTokenUtil;
import org.bbqqvv.backendeducation.service.auth.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;



@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final CustomUserDetailsService customUserDetailsService;
	
	private final JwtTokenUtil jwtTokenUtil;
	
	public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }
	
	// Reduced whitelist for simplicity
    private static final String[] WHITE_LIST_URL = {
    		"/auth/login",   // Đăng nhập
            "/auth/register" // Đăng ký
    };
	
    private static final String[] SECURED_URL_PATTERNS = {
    		"/api/**", 
    		"/admin/**", 
    		"/user/**"
    };

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests((authorize) -> 
					authorize
					.requestMatchers(WHITE_LIST_URL).permitAll() // Cho phép không cần xác thực cho register và login
            		.requestMatchers(SECURED_URL_PATTERNS).authenticated()) // Yêu cầu xác thực cho api còn lại
			.addFilterBefore(new JwtAuthenticationFilter(jwtTokenUtil, customUserDetailsService), UsernamePasswordAuthenticationFilter.class)
			.formLogin(
                    form -> form
                    .loginPage("/login")
                    .defaultSuccessUrl("/api/projects", true)
                    .permitAll())
		    .logout(
	                logout -> logout
	                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
	                        .permitAll()
	        );
        return http.build();
    }
	
	@Bean
    AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = 
            http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService)
            .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

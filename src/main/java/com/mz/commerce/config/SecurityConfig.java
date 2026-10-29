package com.mz.commerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig { // 또는 기존에 있는 보안 설정 클래스

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	//test용
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http)
		throws Exception {

		http
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(auth -> auth
				.anyRequest().permitAll()
			);
		return http.build();
	}
}
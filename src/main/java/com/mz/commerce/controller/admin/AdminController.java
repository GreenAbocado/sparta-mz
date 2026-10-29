package com.mz.commerce.controller.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mz.commerce.common.authentication.SessionUser;
import com.mz.commerce.dto.admin.AdminLoginRequest;
import com.mz.commerce.dto.admin.AdminLoginResponse;
import com.mz.commerce.dto.admin.AdminRegisterRequest;
import com.mz.commerce.dto.admin.AdminRegisterResponse;
import com.mz.commerce.service.admin.AdminService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminController {
	private final AdminService adminService;

	@PostMapping("/admins/register")
	public ResponseEntity<AdminRegisterResponse> registerAdmin(
		@RequestBody @Valid AdminRegisterRequest request
	) {
		AdminRegisterResponse response = adminService.registerAdmin(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/admins/login")
	public ResponseEntity<AdminLoginResponse> loginAdmin(
		@RequestBody @Valid AdminLoginRequest request, HttpSession session
	) {
		SessionUser sessionUser = adminService.login(request, session);
		AdminLoginResponse response = new AdminLoginResponse(
			sessionUser.getId(),
			sessionUser.getRole()
		);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpSession session) {
		session.invalidate();
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
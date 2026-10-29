package com.mz.commerce.controller.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mz.commerce.common.authentication.Login;
import com.mz.commerce.common.authentication.SessionUser;
import com.mz.commerce.common.authentication.SuperAdmin;
import com.mz.commerce.dto.admin.AdminLoginRequest;
import com.mz.commerce.dto.admin.AdminLoginResponse;
import com.mz.commerce.dto.admin.AdminRegisterRequest;
import com.mz.commerce.dto.admin.AdminRegisterResponse;
import com.mz.commerce.dto.admin.AdminResponse;
import com.mz.commerce.dto.admin.AdminRoleUpdateRequest;
import com.mz.commerce.dto.admin.AdminRoleUpdateResponse;
import com.mz.commerce.dto.admin.AdminStatusUpdateRequest;
import com.mz.commerce.dto.admin.AdminStatusUpdateResponse;
import com.mz.commerce.dto.admin.AdminUpdateRequest;
import com.mz.commerce.dto.admin.AdminUpdateResponse;
import com.mz.commerce.service.admin.AdminService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminController {
	private final AdminService adminService;

	// 회원가입
	@PostMapping("/admins/register")
	public ResponseEntity<AdminRegisterResponse> registerAdmin(
		@RequestBody @Valid AdminRegisterRequest request
	) {
		AdminRegisterResponse response = adminService.registerAdmin(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// 로그인
	@PostMapping("/admins/login")
	public ResponseEntity<AdminLoginResponse> loginAdmin(
		@RequestBody @Valid AdminLoginRequest request, HttpSession session
	) {
		AdminLoginResponse response = adminService.login(request);
		SessionUser sessionUser = new SessionUser(response.getId(), response.getAdminRole());
		session.setAttribute("LOGIN_USER", sessionUser);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	// 로그아웃
	@PostMapping("/admins/logout")
	public ResponseEntity<Void> logout(HttpSession session) {
		session.invalidate();
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	// 관리자 단건 조회
	@PostMapping("/admins/{id}")
	public ResponseEntity<AdminResponse> getAdminById(
		@PathVariable Long id, HttpSession session
	) {
		AdminResponse response = adminService.getOne(id);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	// 관리자 수정 (이름, 이메일, 번호)
	@PutMapping("/admins/{id}")
	public ResponseEntity<AdminUpdateResponse> updateAdmin(
		@PathVariable Long id,
		@RequestBody @Valid AdminUpdateRequest request
	) {
		AdminUpdateResponse response = adminService.updateAdmin(id, request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	// 관리자 상태 수정
	@SuperAdmin
	@PutMapping("/admins/{id}/status")
	public ResponseEntity<AdminStatusUpdateResponse> updateStatusAdmin(
		@PathVariable Long id,
		@RequestBody @Valid AdminStatusUpdateRequest request,
		@Login SessionUser sessionUser
	) {
		AdminStatusUpdateResponse response = adminService.updateAdminStatus(id, request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	// 관리자 역할 수정
	@PutMapping("/admins/{id}/role")
	public ResponseEntity<AdminRoleUpdateResponse> updateRoleAdmin(
		@PathVariable Long id,
		@RequestBody @Valid AdminRoleUpdateRequest request
	) {
		AdminRoleUpdateResponse response = adminService.updateAdminRole(id, request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping("/admins/{id}")
	public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
		adminService.adminDelete(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
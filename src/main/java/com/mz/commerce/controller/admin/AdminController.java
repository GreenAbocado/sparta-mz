package com.mz.commerce.controller.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mz.commerce.common.authentication.HigherAdmin;
import com.mz.commerce.common.authentication.Login;
import com.mz.commerce.common.authentication.SessionConst;
import com.mz.commerce.common.authentication.SessionUser;
import com.mz.commerce.dto.admin.AdminLoginRequest;
import com.mz.commerce.dto.admin.AdminLoginResponse;
import com.mz.commerce.dto.admin.AdminPasswordUpdateRequest;
import com.mz.commerce.dto.admin.AdminRegisterRequest;
import com.mz.commerce.dto.admin.AdminRegisterResponse;
import com.mz.commerce.dto.admin.AdminRejectRequest;
import com.mz.commerce.dto.admin.AdminResponse;
import com.mz.commerce.dto.admin.AdminRoleUpdateRequest;
import com.mz.commerce.dto.admin.AdminRoleUpdateResponse;
import com.mz.commerce.dto.admin.AdminStatusUpdateRequest;
import com.mz.commerce.dto.admin.AdminStatusUpdateResponse;
import com.mz.commerce.dto.admin.AdminUpdateRequest;
import com.mz.commerce.dto.admin.AdminUpdateResponse;
import com.mz.commerce.dto.common.PageResponse;
import com.mz.commerce.entity.admin.AdminRole;
import com.mz.commerce.entity.admin.AdminStatus;
import com.mz.commerce.service.admin.AdminService;

import jakarta.servlet.http.HttpServletRequest;
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
		return ResponseEntity.status(HttpStatus.CREATED).body(adminService.registerAdmin(request));
	}

	@PostMapping("/admins/login")
	public ResponseEntity<AdminLoginResponse> loginAdmin(
		@RequestBody @Valid AdminLoginRequest request, HttpServletRequest servletRequest
	) {
		AdminLoginResponse response = adminService.login(request);
		HttpSession oldSession = servletRequest.getSession(false);
		if (oldSession != null) {
			oldSession.invalidate();

		}
		HttpSession newSession = servletRequest.getSession(true);
		SessionUser sessionUser = new SessionUser(response.getId(), response.getAdminRole());
		newSession.setAttribute(SessionConst.SESSION_USER, sessionUser);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping("/admins/logout")
	public ResponseEntity<Void> logout(HttpSession session) {
		session.invalidate();
		return ResponseEntity.ok().build();
	}

	@HigherAdmin(AdminRole.SUPER)
	@GetMapping("/admins")
	public ResponseEntity<PageResponse<AdminResponse>> getAllAdmins(
		@RequestParam(required = false) String keyword,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "createdAt") String sortBy,
		@RequestParam(defaultValue = "desc") String sortOrder,
		@RequestParam(required = false) AdminRole role,
		@RequestParam(required = false) AdminStatus status
	) {
		return ResponseEntity.ok(adminService.getAll(keyword, page, size, sortBy, sortOrder, role, status));
	}

	@HigherAdmin(AdminRole.SUPER)
	@GetMapping("/admins/{id}")
	public ResponseEntity<AdminResponse> getAdminById(@PathVariable Long id) {
		return ResponseEntity.ok(adminService.getOne(id));
	}

	@GetMapping("/admins/profile")
	public ResponseEntity<AdminResponse> getAdminProfile(@Login SessionUser sessionUser) {
		return ResponseEntity.ok(adminService.getProfile(sessionUser.getId()));
	}

	@PutMapping("/admins/{id}")
	public ResponseEntity<AdminUpdateResponse> updateAdmin(
		@PathVariable Long id,
		@RequestBody @Valid AdminUpdateRequest request
	) {
		return ResponseEntity.ok(adminService.updateAdmin(id, request));
	}

	@HigherAdmin(AdminRole.SUPER)
	@PutMapping("/admins/{id}/status")
	public ResponseEntity<AdminStatusUpdateResponse> updateStatusAdmin(
		@PathVariable Long id,
		@RequestBody @Valid AdminStatusUpdateRequest request
	) {
		return ResponseEntity.ok(adminService.updateAdminStatus(id, request));
	}

	@HigherAdmin(AdminRole.SUPER)
	@PutMapping("/admins/{id}/role")
	public ResponseEntity<AdminRoleUpdateResponse> updateRoleAdmin(
		@PathVariable Long id,
		@RequestBody @Valid AdminRoleUpdateRequest request
	) {
		return ResponseEntity.ok(adminService.updateAdminRole(id, request));
	}

	@HigherAdmin(AdminRole.SUPER)
	@PutMapping("/admins/{id}/approve")
	public ResponseEntity<AdminResponse> approveAdmin(@PathVariable Long id) {
		return ResponseEntity.ok(adminService.approveAdmin(id));
	}

	@HigherAdmin(AdminRole.SUPER)
	@PutMapping("/admins/{id}/reject")
	public ResponseEntity<AdminResponse> rejectAdmin(
		@PathVariable Long id,
		@Valid @RequestBody AdminRejectRequest request
	) {
		return ResponseEntity.ok(adminService.rejectAdmin(id, request.getRejectionReason()));
	}

	@HigherAdmin(AdminRole.SUPER)
	@DeleteMapping("/admins/{id}")
	public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
		adminService.adminDelete(id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/admins/profile")
	public ResponseEntity<AdminResponse> updateAdminProfile(
		@RequestBody @Valid AdminUpdateRequest request,
		@Login SessionUser sessionUser
	) {
		AdminResponse response = adminService.updateAdminProfile(sessionUser.getId(), request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PutMapping("/admins/profile/password")
	public ResponseEntity<Void> updateAdminPassword(
		@RequestBody @Valid AdminPasswordUpdateRequest request,
		@Login SessionUser sessionUser
	) {
		adminService.updateAdminPassword(sessionUser.getId(), request);
		return ResponseEntity.ok().build();
	}
}
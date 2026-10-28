package com.mz.commerce.service.admin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.commerce.common.authentication.SessionUser;
import com.mz.commerce.common.exception.CustomException;
import com.mz.commerce.common.exception.ErrorCode;
import com.mz.commerce.dto.admin.AdminLoginRequest;
import com.mz.commerce.dto.admin.AdminRegisterRequest;
import com.mz.commerce.dto.admin.AdminRegisterResponse;
import com.mz.commerce.entity.admin.Admin;
import com.mz.commerce.entity.admin.AdminRole;
import com.mz.commerce.entity.admin.AdminStatus;
import com.mz.commerce.repository.admin.AdminRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
	private final AdminRepository adminRepository;

	// 관리자 회원가입 서비스 로직
	@Transactional
	public AdminRegisterResponse registerAdmin(AdminRegisterRequest request) {
		if (adminRepository.existsByEmail(request.getEmail())) {
			throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
		}
		Admin admin = new Admin(
			request.getName(),
			request.getEmail(),
			request.getPassword(),
			request.getPhone(),
			AdminStatus.PENDING,
			AdminRole.OPERATION
		);
		Admin savedAdmin = adminRepository.save(admin);
		return new AdminRegisterResponse(savedAdmin);
	}

	@Transactional(readOnly = true)
	public SessionUser login(AdminLoginRequest request, HttpSession session) {
		Admin admin = adminRepository.findByEmail(request.getEmail()).orElseThrow(
			() -> new CustomException(ErrorCode.EMAIL_NOT_FOUND)
		);
		if (!admin.getPassword().equals(request.getPassword())) {
			throw new CustomException(ErrorCode.INVALID_PASSWORD);
		}
		if (admin.getStatus() != AdminStatus.ACTIVE) {
			throw new CustomException(ErrorCode.ADMIN_NOT_ACTIVE);
		}
		SessionUser sessionUser = new SessionUser(admin.getId(), admin.getRole());
		session.setAttribute("LOGIN_ADMIN", sessionUser);
		return sessionUser;
	}
}
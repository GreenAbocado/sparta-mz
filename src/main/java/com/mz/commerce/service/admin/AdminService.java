package com.mz.commerce.service.admin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.commerce.common.exception.CustomException;
import com.mz.commerce.common.exception.ErrorCode;
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
import com.mz.commerce.entity.admin.Admin;
import com.mz.commerce.entity.admin.AdminRole;
import com.mz.commerce.entity.admin.AdminStatus;
import com.mz.commerce.repository.admin.AdminRepository;

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
	public AdminLoginResponse login(AdminLoginRequest request) {
		Admin admin = adminRepository.findByEmail(request.getEmail()).orElseThrow(
			() -> new CustomException(ErrorCode.EMAIL_NOT_FOUND)
		);
		if (!admin.getPassword().equals(request.getPassword())) {
			throw new CustomException(ErrorCode.INVALID_PASSWORD);
		}
		if (admin.getStatus() != AdminStatus.ACTIVE) {
			throw new CustomException(ErrorCode.ADMIN_NOT_ACTIVE);
		}
		return new AdminLoginResponse(admin.getId(), admin.getRole());
	}

	@Transactional(readOnly = true)
	public AdminResponse getOne(Long id) {
		Admin admin = adminRepository.findById(id).orElseThrow(
			() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND)
		);
		return new AdminResponse(admin);
	}

	@Transactional
	public AdminUpdateResponse updateAdmin(Long id, AdminUpdateRequest request) {
		Admin admin = adminRepository.findById(id).orElseThrow(
			() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND)
		);
		if (!admin.getEmail().equals(request.getEmail())) {
			if (adminRepository.existsByEmail(request.getEmail())) {
				throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
			}
		}
		admin.update(request.getName(), request.getEmail(), request.getPhone());
		return new AdminUpdateResponse(admin);
	}

	/**
	 * 상태 변경 시 REJECTED면 RejectionReason에 값이 들어가야하고
	 * 그 외의 상태에서는 null값이 들어가야한다.
	 */
	@Transactional
	public AdminStatusUpdateResponse updateAdminStatus(Long id, AdminStatusUpdateRequest request) {
		Admin admin = adminRepository.findById(id).orElseThrow(
			() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND)
		);
		String rejectionReason = null;
		if (request.getStatus() == AdminStatus.REJECTED) {
			if (request.getRejectionReason() == null || request.getRejectionReason().isBlank()) {
				throw new CustomException(ErrorCode.REJECTION_REASON_REQUIRED);
			}
			rejectionReason = request.getRejectionReason();
		} else {
			rejectionReason = null;
		}
		admin.updateStatus(request.getStatus(), request.getRejectionReason());

		return new AdminStatusUpdateResponse(admin);
	}

	@Transactional
	public AdminRoleUpdateResponse updateAdminRole(Long id, AdminRoleUpdateRequest request) {
		Admin admin = adminRepository.findById(id).orElseThrow(
			() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND)
		);
		admin.updateRole(request.getRole());

		return new AdminRoleUpdateResponse(admin);
	}

	@Transactional
	public void adminDelete(Long id) {
		Admin admin = adminRepository.findById(id).orElseThrow(
			() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND)
		);
		admin.delete();
	}
}
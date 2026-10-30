package com.mz.commerce.service;

import java.util.Locale;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.commerce.common.exception.CustomException;
import com.mz.commerce.common.exception.ErrorCode;
import com.mz.commerce.dto.admin.AdminLoginRequest;
import com.mz.commerce.dto.admin.AdminLoginResponse;
import com.mz.commerce.dto.admin.AdminPasswordUpdateRequest;
import com.mz.commerce.dto.admin.AdminRegisterRequest;
import com.mz.commerce.dto.admin.AdminRegisterResponse;
import com.mz.commerce.dto.admin.AdminResponse;
import com.mz.commerce.dto.admin.AdminRoleUpdateRequest;
import com.mz.commerce.dto.admin.AdminRoleUpdateResponse;
import com.mz.commerce.dto.admin.AdminStatusUpdateRequest;
import com.mz.commerce.dto.admin.AdminStatusUpdateResponse;
import com.mz.commerce.dto.admin.AdminUpdateRequest;
import com.mz.commerce.dto.admin.AdminUpdateResponse;
import com.mz.commerce.dto.common.PageResponse;
import com.mz.commerce.entity.admin.Admin;
import com.mz.commerce.entity.admin.AdminRole;
import com.mz.commerce.entity.admin.AdminStatus;
import com.mz.commerce.repository.AdminRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

	private final AdminRepository adminRepository;
	private final PasswordEncoder passwordEncoder;

	private static final Set<String> SORT_FIELDS =
		Set.of("createdAt", "name", "email", "role", "status");

	@Transactional
	public AdminRegisterResponse registerAdmin(AdminRegisterRequest request) {
		if (adminRepository.existsByEmail(request.getEmail())) {
			throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
		}
		if (request.getPassword().length() < 8) {
			throw new CustomException(ErrorCode.WEAK_PASSWORD);
		}

		if (request.getRole() == AdminRole.SUPER) {
			throw new CustomException(ErrorCode.INVALID_ADMIN_ROLE);
		}

		Admin admin = new Admin(
			request.getName(),
			request.getEmail(),
			passwordEncoder.encode(request.getPassword()),
			request.getPhone(),
			AdminStatus.PENDING,
			//AdminRole.OPERATION
			request.getRole()
		);

		return new AdminRegisterResponse(adminRepository.save(admin));
	}

	// 로그인: 인증된 관리자 정보를 세션에 저장
	@Transactional(readOnly = true)
	public AdminLoginResponse login(AdminLoginRequest request) {
		Admin admin = adminRepository.findByEmail(request.getEmail())
			.orElseThrow(() -> new CustomException(ErrorCode.INVALID_LOGIN));

		if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
			throw new CustomException(ErrorCode.INVALID_LOGIN);
		}

		if (admin.getDeleted() != null) {
			throw new CustomException(ErrorCode.ADMIN_NOT_FOUND);
		}

		switch (admin.getStatus()) {
			case ACTIVE:
				return new AdminLoginResponse(admin.getId(), admin.getRole());
			case PENDING:
				throw new CustomException(ErrorCode.ADMIN_PENDING);
			case REJECTED:
				throw new CustomException(ErrorCode.ADMIN_REJECTED);
			case SUSPENDED:
				throw new CustomException(ErrorCode.ADMIN_SUSPENDED);
			case INACTIVE:
				throw new CustomException(ErrorCode.ADMIN_INACTIVE);
			default:
				throw new CustomException(ErrorCode.WRONG_ACCESS);
		}

	}

	// 관리자 목록: 검색, 필터, 정렬, 페이지네이션
	@Transactional(readOnly = true)
	public PageResponse<AdminResponse> getAll(
		String keyword,
		int page,
		int size,
		String sortBy,
		String sortOrder,
		AdminRole role,
		AdminStatus status
	) {
		if (page < 1 || size < 1) {
			throw new CustomException(ErrorCode.INVALID_PAGE_REQUEST);
		}

		if (!SORT_FIELDS.contains(sortBy)) {
			throw new CustomException(ErrorCode.INVALID_SORT_FIELD);
		}

		Sort.Direction direction;
		try {
			direction = Sort.Direction.fromString(sortOrder);
		} catch (IllegalArgumentException e) {
			throw new CustomException(ErrorCode.INVALID_SORT_ORDER);
		}

		Pageable pageable = PageRequest.of(
			page - 1,
			size,
			Sort.by(direction, sortBy)
		);

		Specification<Admin> spec = (root, query, cb) ->
			cb.isNull(root.get("deleted"));

		if (keyword != null && !keyword.isBlank()) {
			String searchKeyword =
				"%" + keyword.trim().toLowerCase(Locale.ROOT) + "%";

			spec = spec.and((root, query, cb) ->
				cb.or(
					cb.like(
						cb.lower(root.get("name")),
						searchKeyword
					),
					cb.like(
						cb.lower(root.get("email")),
						searchKeyword
					)
				)
			);
		}

		if (role != null) {
			spec = spec.and((root, query, cb) ->
				cb.equal(root.get("role"), role)
			);
		}

		if (status != null) {
			spec = spec.and((root, query, cb) ->
				cb.equal(root.get("status"), status)
			);
		}

		Page<Admin> adminPage = adminRepository.findAll(spec, pageable);

		return new PageResponse<>(
			adminPage.map(AdminResponse::new)
		);
	}

	// 관리자 단건 조회
	@Transactional(readOnly = true)
	public AdminResponse getOne(Long id) {
		Admin admin = findAdmin(id);
		return new AdminResponse(admin);
	}

	// 로그인한 관리자 본인 프로필
	@Transactional(readOnly = true)
	public AdminResponse getProfile(Long id) {
		return new AdminResponse(findAdmin(id));
	}

	@Transactional
	public AdminUpdateResponse updateAdmin(
		Long sessionId,
		Long id,
		AdminUpdateRequest request
	) {
		Admin admin = findAdmin(id);

		// 본인 및 다른 SUPER 관리자 대상 작업 방지
		validateAdminOperation(sessionId, admin);

		if (!admin.getEmail().equals(request.getEmail())
			&& adminRepository.existsByEmail(request.getEmail())) {
			throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
		}

		admin.update(
			request.getName(),
			request.getEmail(),
			request.getPhone()
		);

		return new AdminUpdateResponse(admin);
	}

	@Transactional
	public AdminStatusUpdateResponse updateAdminStatus(
		Long sessionId,
		Long id,
		AdminStatusUpdateRequest request
	) {
		Admin admin = findAdmin(id);

		// 본인 및 다른 SUPER 관리자 대상 작업 방지
		validateAdminOperation(sessionId, admin);

		AdminStatus status = request.getStatus();
		String rejectionReason = request.getRejectionReason();

		if (status == AdminStatus.REJECTED
			&& (rejectionReason == null || rejectionReason.isBlank())) {
			throw new CustomException(ErrorCode.REJECTION_REASON_REQUIRED);
		}

		if (status != AdminStatus.REJECTED) {
			rejectionReason = null;
		}

		admin.updateStatus(status, rejectionReason);
		return new AdminStatusUpdateResponse(admin);
	}

	@Transactional
	public AdminRoleUpdateResponse updateAdminRole(
		Long sessionId,
		Long id,
		AdminRoleUpdateRequest request
	) {
		Admin admin = findAdmin(id);

		// 본인 및 다른 SUPER 관리자 대상 작업 방지
		validateAdminOperation(sessionId, admin);

		admin.updateRole(request.getRole());

		return new AdminRoleUpdateResponse(admin);
	}

	// 가입 승인: Admin 엔티티의 승인 메서드 사용
	@Transactional
	public AdminResponse approveAdmin(Long sessionId, Long id) {
		Admin admin = findAdmin(id);

		// 본인 및 다른 SUPER 관리자 대상 작업 방지
		validateAdminOperation(sessionId, admin);

		admin.approve();
		return new AdminResponse(admin);
	}

	// 가입 반려: 반려 사유 필수
	@Transactional
	public AdminResponse rejectAdmin(Long sessionId, Long id, String rejectionReason) {
		Admin admin = findAdmin(id);

		// 본인 및 다른 SUPER 관리자 대상 작업 방지
		validateAdminOperation(sessionId, admin);

		admin.reject(rejectionReason);
		return new AdminResponse(admin);
	}

	@Transactional
	public AdminResponse updateAdminProfile(Long id, AdminUpdateRequest request) {
		Admin admin = adminRepository.findById(id).orElseThrow(
			() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND)
		);
		admin.update(request.getName(), request.getEmail(), request.getPhone());
		return new AdminResponse(admin);
	}

	@Transactional
	public void updateAdminPassword(Long adminId, AdminPasswordUpdateRequest request) {
		Admin admin = adminRepository.findById(adminId).orElseThrow(
			() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND)
		);
		if (!passwordEncoder.matches(request.getCurrentPassword(), admin.getPassword())) {
			throw new CustomException(ErrorCode.INVALID_PASSWORD);
		}
		String encodedNewPassword = passwordEncoder.encode(request.getNewPassword());
		admin.updatePassword(encodedNewPassword);
	}

	@Transactional
	public void adminDelete(Long sessionId, Long id) {
		Admin admin = findAdmin(id);

		// 본인 및 다른 SUPER 관리자 대상 작업 방지
		validateAdminOperation(sessionId, admin);

		admin.delete();
	}

	private Admin findAdmin(Long id) {
		return adminRepository.findByIdAndDeletedIsNull(id)
			.orElseThrow(() ->
				new CustomException(ErrorCode.ADMIN_NOT_FOUND)
			);
	}

	private void validateAdminOperation(Long requesterId, Admin targetAdmin) {
		// 본인 대상 작업 방지
		if (requesterId.equals(targetAdmin.getId())) {
			throw new CustomException(ErrorCode.ADMIN_OPERATION_FORBIDDEN);
		}

		// 다른 SUPER 관리자 대상 작업 방지
		if (targetAdmin.getRole() == AdminRole.SUPER) {
			throw new CustomException(ErrorCode.ADMIN_OPERATION_FORBIDDEN);
		}
	}
}

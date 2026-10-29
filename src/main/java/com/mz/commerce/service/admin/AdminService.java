package com.mz.commerce.service.admin;

import java.util.Locale;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.commerce.common.authentication.SessionConst;
import com.mz.commerce.common.authentication.SessionUser;
import com.mz.commerce.common.exception.CustomException;
import com.mz.commerce.common.exception.ErrorCode;
import com.mz.commerce.dto.admin.AdminLoginRequest;
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
import com.mz.commerce.repository.admin.AdminRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

	private final AdminRepository adminRepository;

	private static final Set<String> SORT_FIELDS =
		Set.of("createdAt", "name", "email", "role", "status");

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

		return new AdminRegisterResponse(adminRepository.save(admin));
	}

	// 로그인: 인증된 관리자 정보를 세션에 저장
	@Transactional(readOnly = true)
	public SessionUser login(AdminLoginRequest request, HttpSession session) {
		Admin admin = adminRepository.findByEmail(request.getEmail())
			.orElseThrow(() -> new CustomException(ErrorCode.EMAIL_NOT_FOUND));

		if (!admin.getPassword().equals(request.getPassword())) {
			throw new CustomException(ErrorCode.INVALID_PASSWORD);
		}

		if (admin.getStatus() != AdminStatus.ACTIVE) {
			throw new CustomException(ErrorCode.ADMIN_NOT_ACTIVE);
		}

		SessionUser sessionUser =
			new SessionUser(admin.getId(), admin.getRole());

		session.setAttribute(SessionConst.SESSION_USER, sessionUser);
		return sessionUser;
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
			throw new IllegalArgumentException(
				"페이지와 페이지 크기는 1 이상이어야 합니다."
			);
		}

		if (!SORT_FIELDS.contains(sortBy)) {
			throw new IllegalArgumentException(
				"지원하지 않는 정렬 필드입니다: " + sortBy
			);
		}

		Sort.Direction direction;
		try {
			direction = Sort.Direction.fromString(sortOrder);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(
				"정렬 순서는 asc 또는 desc여야 합니다."
			);
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
		Long id,
		AdminUpdateRequest request
	) {
		Admin admin = findAdmin(id);

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
		Long id,
		AdminStatusUpdateRequest request
	) {
		Admin admin = findAdmin(id);
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
		Long id,
		AdminRoleUpdateRequest request
	) {
		Admin admin = findAdmin(id);
		admin.updateRole(request.getRole());

		return new AdminRoleUpdateResponse(admin);
	}

	// 가입 승인: Admin 엔티티의 승인 메서드 사용
	@Transactional
	public AdminResponse approveAdmin(Long id) {
		Admin admin = findAdmin(id);

		if (admin.getStatus() == AdminStatus.ACTIVE) {
			throw new CustomException(ErrorCode.ADMIN_ALREADY_ACTIVE);
		}

		if (admin.getStatus() == AdminStatus.REJECTED) {
			throw new CustomException(ErrorCode.ADMIN_ALREADY_REJECTED);
		}

		if (admin.getStatus() != AdminStatus.PENDING) {
			throw new CustomException(ErrorCode.ADMIN_APPROVAL_NOT_ALLOWED);
		}

		admin.approve();
		return new AdminResponse(admin);
	}

	// 가입 반려: 반려 사유 필수
	@Transactional
	public AdminResponse rejectAdmin(Long id, String rejectionReason) {
		Admin admin = findAdmin(id);

		if (rejectionReason == null || rejectionReason.isBlank()) {
			throw new CustomException(ErrorCode.REJECTION_REASON_REQUIRED);
		}

		if (admin.getStatus() == AdminStatus.REJECTED) {
			throw new CustomException(ErrorCode.ADMIN_ALREADY_REJECTED);
		}

		if (admin.getStatus() == AdminStatus.ACTIVE) {
			throw new CustomException(ErrorCode.ADMIN_ALREADY_ACTIVE);
		}

		if (admin.getStatus() != AdminStatus.PENDING) {
			throw new CustomException(ErrorCode.ADMIN_REJECTION_NOT_ALLOWED);
		}

		admin.reject(rejectionReason);
		return new AdminResponse(admin);
	}

	@Transactional
	public void adminDelete(Long id) {
		Admin admin = findAdmin(id);
		admin.delete();
	}

	private Admin findAdmin(Long id) {
		return adminRepository.findById(id)
			.filter(admin -> admin.getDeleted() == null)
			.orElseThrow(() ->
				new CustomException(ErrorCode.ADMIN_NOT_FOUND)
			);
	}
}

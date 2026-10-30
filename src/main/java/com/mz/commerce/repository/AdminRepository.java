package com.mz.commerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.mz.commerce.entity.admin.Admin;
import com.mz.commerce.entity.admin.AdminRole;

public interface AdminRepository
	extends JpaRepository<Admin, Long>,
	JpaSpecificationExecutor<Admin> {
	Optional<Admin> findByEmail(String email);

	boolean existsByEmail(String email);

	Optional<Admin> findByIdAndDeletedIsNull(Long id);

	/**
	 * Soft Delete가 안되었으면서 해당 ID와 Role을 갖는 관리자를 찾습니다.
	 *
	 * <p> {@link com.mz.commerce.common.authentication.AuthenticationInterceptor}에서 세션, DB 불일치를 해소합니다.
	 */
	boolean existsByIdAndRoleAndDeletedIsNull(Long id, AdminRole role);
}

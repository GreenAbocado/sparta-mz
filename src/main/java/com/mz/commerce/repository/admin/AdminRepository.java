package com.mz.commerce.repository.admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mz.commerce.entity.admin.Admin;
import com.mz.commerce.entity.admin.AdminRole;

public interface AdminRepository extends JpaRepository<Admin, Long> {
	boolean existsByEmail(String email);

	Optional<Admin> findByEmail(String email);

	/**
	 * ID와 Role을 받고 해당 정보를 갖으면서 삭제가 안 된 Admin을 찾습니다.
	 *
	 * <p>{@link com.mz.commerce.common.authentication.AuthenticationInterceptor}에서 사용됩니다.
	 * 세션 정보와 DB 정보의 불일치를 해소하기 위해 매 요청마다 인터셉터에서 확인합니다.
	 */
	boolean existsByIdAndRoleAndDeletedIsNull(Long id, AdminRole role);
}
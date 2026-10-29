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

	boolean existsByIdAndRoleAndDeletedIsNull(Long id, AdminRole role);

	Optional<Admin> findByIdAndDeletedIsNull(Long id);
}

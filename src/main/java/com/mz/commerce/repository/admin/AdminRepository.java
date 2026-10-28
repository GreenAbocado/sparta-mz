package com.mz.commerce.repository.admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mz.commerce.entity.admin.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
	boolean existsByEmail(String email);

	Optional<Admin> findByEmail(String email);
}

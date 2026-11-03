package com.mz.commerce.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mz.commerce.entity.admin.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}

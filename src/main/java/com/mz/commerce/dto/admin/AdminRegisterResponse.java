package com.mz.commerce.dto.admin;

import java.time.LocalDateTime;

import com.mz.commerce.entity.admin.Admin;
import com.mz.commerce.entity.admin.AdminRole;
import com.mz.commerce.entity.admin.AdminStatus;

import lombok.Getter;

@Getter
public class AdminRegisterResponse {
	private final Long id;
	private final String name;
	private final String email;
	private final String phone;
	private final AdminStatus status;
	private final AdminRole role;
	private final LocalDateTime createdAt;

	public AdminRegisterResponse(Admin admin) {
		this.id = admin.getId();
		this.name = admin.getName();
		this.email = admin.getEmail();
		this.phone = admin.getPhone();
		this.status = admin.getStatus();
		this.role = admin.getRole();
		this.createdAt = admin.getCreatedAt();
	}
}

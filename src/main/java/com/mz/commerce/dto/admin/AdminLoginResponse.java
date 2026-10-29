package com.mz.commerce.dto.admin;

import com.mz.commerce.entity.admin.AdminRole;

import lombok.Getter;

@Getter
public class AdminLoginResponse {
	private final Long id;
	private final AdminRole adminRole;

	public AdminLoginResponse(Long id, AdminRole adminRole) {
		this.id = id;
		this.adminRole = adminRole;
	}
}

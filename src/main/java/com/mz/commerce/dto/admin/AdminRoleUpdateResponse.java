package com.mz.commerce.dto.admin;

import com.mz.commerce.entity.admin.Admin;
import com.mz.commerce.entity.admin.AdminRole;

import lombok.Getter;

@Getter
public class AdminRoleUpdateResponse {
	private Long id;
	private AdminRole role;

	public AdminRoleUpdateResponse(Admin admin) {
		this.id = admin.getId();
		this.role = admin.getRole();
	}
}

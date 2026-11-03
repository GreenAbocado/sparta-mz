package com.mz.commerce.dto.admin;

import java.time.LocalDateTime;

import com.mz.commerce.entity.admin.Admin;
import com.mz.commerce.entity.admin.AdminRole;
import com.mz.commerce.entity.admin.AdminStatus;

import lombok.Getter;

@Getter
public class AdminUpdateResponse {
	private Long id;
	private String name;
	private String email;
	private String phone;
	private AdminStatus status;
	private AdminRole role;
	private LocalDateTime approvedAt;
	private LocalDateTime rejectedAt;
	private String rejectionReason;

	public AdminUpdateResponse(Admin admin) {
		this.id = admin.getId();
		this.name = admin.getName();
		this.email = admin.getEmail();
		this.phone = admin.getPhone();
		this.status = admin.getStatus();
		this.role = admin.getRole();
		this.approvedAt = admin.getApprovedAt();
		this.rejectedAt = admin.getRejectedAt();
		this.rejectionReason = admin.getRejectionReason();
	}
}

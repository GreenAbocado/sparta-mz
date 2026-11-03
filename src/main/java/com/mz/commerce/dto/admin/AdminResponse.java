package com.mz.commerce.dto.admin;

import java.time.LocalDateTime;

import com.mz.commerce.entity.admin.Admin;
import com.mz.commerce.entity.admin.AdminRole;
import com.mz.commerce.entity.admin.AdminStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AdminResponse {
	private final Long id;
	private final String name;
	private final String email;
	private final String phone;
	private final AdminStatus status;
	private final AdminRole role;
	private final LocalDateTime createdAt;
	private final LocalDateTime approvedAt;
	private final LocalDateTime rejectedAt;
	private final String rejectionReason;

	public AdminResponse(Admin admin) {
		this.id = admin.getId();
		this.name = admin.getName();
		this.email = admin.getEmail();
		this.phone = admin.getPhone();
		this.status = admin.getStatus();
		this.role = admin.getRole();
		this.createdAt = admin.getCreatedAt();
		this.approvedAt = admin.getApprovedAt();
		this.rejectedAt = admin.getRejectedAt();
		this.rejectionReason = admin.getRejectionReason();
	}
}

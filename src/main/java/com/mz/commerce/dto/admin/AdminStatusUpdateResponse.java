package com.mz.commerce.dto.admin;

import com.mz.commerce.entity.admin.Admin;
import com.mz.commerce.entity.admin.AdminStatus;

import lombok.Getter;

@Getter
public class AdminStatusUpdateResponse {
	private Long id;
	private AdminStatus status;
	private String rejectionReason;

	public AdminStatusUpdateResponse(Admin admin) {
		this.id = admin.getId();
		this.status = admin.getStatus();
		this.rejectionReason = admin.getRejectionReason();
	}
}

package com.mz.commerce.dto.admin;

import com.mz.commerce.entity.admin.AdminStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminStatusUpdateRequest {
	@NotNull(message = "변경할 상태 값은 필수입니다.")
	private AdminStatus status;

	private String rejectionReason;
}

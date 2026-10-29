package com.mz.commerce.dto.admin;

import com.mz.commerce.entity.admin.AdminRole;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminRoleUpdateRequest {
	@NotNull(message = "변경할 역할 값은 필수입니다.")
	private AdminRole role;
}

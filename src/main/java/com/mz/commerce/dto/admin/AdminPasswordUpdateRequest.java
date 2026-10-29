package com.mz.commerce.dto.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminPasswordUpdateRequest {
	@NotBlank(message = "현재 비밀번호는 필수입니다.")
	private String currentPassword;

	@NotBlank(message = "새로운 비밀번호는 필수입니다.")
	private String newPassword;
}

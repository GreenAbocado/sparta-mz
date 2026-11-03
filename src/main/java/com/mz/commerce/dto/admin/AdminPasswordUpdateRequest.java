package com.mz.commerce.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminPasswordUpdateRequest {
	@Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
	@NotBlank(message = "현재 비밀번호는 필수입니다.")
	private String currentPassword;

	@Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
	@NotBlank(message = "새로운 비밀번호는 필수입니다.")
	private String newPassword;
}

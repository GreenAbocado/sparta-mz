package com.mz.commerce.dto.admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminUpdateRequest {
	@NotBlank(message = "이름은 필수 입력 값입니다.")
	private String name;

	@NotBlank(message = "이메일은 필수 입력 값입니다.")
	@Email(message = "올바른 이메일 형식이 아닙니다.")
	private String email;

	@NotBlank(message = "전화번호는 필수 입력 값입니다.")
	@Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다.")
	private String phone;
}

package com.mz.commerce.dto.admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class AdminRegisterRequest {
	@NotBlank(message = "이름은 필수 입력 항목입니다.")
	private String name;

	@NotBlank(message = "이메일은 필수 입력 항목입니다.")
	@Email(message = "올바른 이메일 형식이어야 합니다.")
	private String email;

	@NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
	@Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
	private String password;

	@NotBlank(message = "전화번호는 필수 입력 항목입니다.")
	@Pattern(regexp = "^010-\\\\d{4}-\\\\d{4}$", message = "올바른 전화번호 형식이 아닙니다.")
	private String phone;

}

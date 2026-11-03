package com.mz.commerce.dto.customer;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CustomerUpdateStatusRequest {
	@NotBlank(message = "고객 상태는 필수 입력 값입니다.")
	private String status;
}

package com.mz.commerce.dto.order;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class OrderDeleteRequest {
	@NotBlank(message = "유효하지 않은 취소 사유 입력입니다.")
	private final String cancelReason;

	public OrderDeleteRequest(String cancelReason) {
		this.cancelReason = (cancelReason == null) ? null : cancelReason.trim();
	}
}

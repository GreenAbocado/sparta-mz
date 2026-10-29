package com.mz.commerce.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class AdminRejectRequest {

	@NotBlank(message = "반려 사유를 입력해 주세요.")
	@Size(max = 255, message = "반려 사유는 255자 이하여야 합니다.")
	private String rejectionReason;
}

package com.mz.commerce.dto.product;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProductUpdateStatusRequest {
	@NotNull(message = "판매 단종 여부를 입력해주세요. (true, false)")
	private Boolean setAsDiscontinued;
}

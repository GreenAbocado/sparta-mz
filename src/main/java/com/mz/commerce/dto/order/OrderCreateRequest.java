package com.mz.commerce.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class OrderCreateRequest {

	@NotNull(message = "유효하지 않은 고객입니다.")
	@Min(value = 1, message = "음수가 입력되었습니다.")
	private final Long customerId;

	@NotNull(message = "유효하지 않은 상품입니다.")
	@Min(value = 1, message = "음수가 입력되었습니다.")
	private final Long productId;

	@NotNull(message = "유효하지 않은 수량입니다.")
	@Min(value = 1, message = "수량은 {value}이상이어야 합니다.")
	private final Integer quantity;
}
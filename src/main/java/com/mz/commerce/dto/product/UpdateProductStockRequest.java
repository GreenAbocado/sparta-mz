package com.mz.commerce.dto.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

@Getter
public class UpdateProductStockRequest {
	@NotNull(message = "재고 수량은 필수입니다.")
	@PositiveOrZero(message = "재고 수량은 0 이상이어야 합니다.")
	private int stock;
}

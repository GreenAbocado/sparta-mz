package com.mz.commerce.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ProductUpdateRequest {
	@NotBlank(message = "상품명은 필수입니다.")
	@Size(max = 100, message = "상품명은 최대 100자까지 입력할 수 있습니다.")
	private String name;

	@NotBlank(message = "상품 카테고리는 필수입니다.")
	private String category;

	@NotNull(message = "상품 가격은 필수입니다.")
	@PositiveOrZero(message = "상품 가격은 0 이상이어야 합니다.")
	private Long price;
}

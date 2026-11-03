package com.mz.commerce.entity.product;

import static com.mz.commerce.common.exception.ErrorCode.*;

import com.mz.commerce.common.exception.CustomException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProductStatus {
	ON_SALE(true),    // 판매 중
	SOLD_OUT(false),   // 품절
	DISCONTINUED(false);    // 판매 중지

	private final boolean orderable;

	public void validateOrderable() {
		if (!orderable) {
			throw new CustomException(PRODUCT_STATUS_NOT_ORDERABLE);
		}
	}
}
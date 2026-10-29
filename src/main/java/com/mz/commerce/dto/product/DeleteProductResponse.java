package com.mz.commerce.dto.product;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DeleteProductResponse {
	private final Long productId;
	private final String name;
	private final LocalDateTime deleted;
}

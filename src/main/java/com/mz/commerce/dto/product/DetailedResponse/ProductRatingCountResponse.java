package com.mz.commerce.dto.product.DetailedResponse;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProductRatingCountResponse {
	private final int rating;
	private final long count;
}

package com.mz.commerce.dto.product.DetailedResponse;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProductRecentReviewResponse {
	private final String customerName;
	private final int rating;
	private final String content;
	private final LocalDateTime createdAt;
}

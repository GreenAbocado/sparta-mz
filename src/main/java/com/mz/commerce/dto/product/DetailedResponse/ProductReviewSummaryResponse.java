package com.mz.commerce.dto.product.DetailedResponse;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProductReviewSummaryResponse {
	private final double averageRating;
	private final long totalReviews;
}

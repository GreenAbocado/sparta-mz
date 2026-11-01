package com.mz.commerce.dto.product.DetailedResponse;

import java.util.List;

import com.mz.commerce.dto.product.ProductResponse;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProductDetailResponse {
	private final ProductResponse productResponse;
	private final ProductReviewSummaryResponse reviewSummary;
	private final List<ProductRatingCountResponse> ratingCounts;
	private final List<ProductRecentReviewResponse> recentReviews;
}

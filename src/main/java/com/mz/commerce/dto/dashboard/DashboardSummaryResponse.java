package com.mz.commerce.dto.dashboard;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DashboardSummaryResponse {
	private final long totalAdminCount;
	private final long activeAdminCount;

	private final long totalCustomerCount;
	private final long activeCustomerCount;

	private final long totalProductCount;
	private final long lowStockProductCount;

	private final long totalOrderCount;
	private final long todayOrderCount;

	private final long totalReviewCount;
	private final Double averageRating;
}

package com.mz.commerce.dto.dashboard;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DashboardSummaryResponse {
	private final Long totalAdminCount;
	private final Long activeAdminCount;

	private final Long totalCustomerCount;
	private final Long activeCustomerCount;

	private final Long totalProductCount;
	private final Long lowStockProductCount;

	private final Long totalOrderCount;
	private final Long todayOrderCount;

	private final Long totalReviewCount;
	private final Double averageRating;
}

package com.mz.commerce.dto.dashboard;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DashboardWidgetResponse {
	private final Long totalSales;
	private final Long todaySales;
	private final Long preparingOrderCount;
	private final Long shippingOrderCount;
	private final Long deliveredOrderCount;
	private final Long lowStockProductCount;
	private final Long soldOutProductCount;
}

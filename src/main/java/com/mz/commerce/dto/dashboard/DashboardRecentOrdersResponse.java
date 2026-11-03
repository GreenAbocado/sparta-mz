package com.mz.commerce.dto.dashboard;

import com.mz.commerce.entity.order.OrderStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DashboardRecentOrdersResponse {
	private final String orderNo;
	private final String customerName;
	private final String productName;
	private final Long totalPrice;
	private final OrderStatus status;
}

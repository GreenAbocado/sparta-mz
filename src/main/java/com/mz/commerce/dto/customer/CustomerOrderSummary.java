package com.mz.commerce.dto.customer;

import lombok.Getter;

@Getter
public class CustomerOrderSummary {
	private final Long totalOrderCount;
	private final Long totalPurchaseAmount;

	public CustomerOrderSummary(Long totalOrderCount, Long totalPurchaseAmount) {
		this.totalOrderCount = totalOrderCount;
		this.totalPurchaseAmount = totalPurchaseAmount == null ? 0L : totalPurchaseAmount;
	}
}

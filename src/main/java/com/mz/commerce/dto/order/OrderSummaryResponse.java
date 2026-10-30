package com.mz.commerce.dto.order;

import java.time.LocalDateTime;

import com.mz.commerce.entity.order.Order;
import com.mz.commerce.entity.order.OrderStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class OrderSummaryResponse {
	private final Long id;
	private final String orderNo;
	private final String customerName;
	private final String productName;
	private final int quantity;
	private final long totalPrice;
	private final LocalDateTime createdAt;
	private final OrderStatus status;
	private final String createdByName;

	// 단일 응답에만 사용 (쿼리 3번 추가 발생)
	public OrderSummaryResponse(Order order) {
		this.id = order.getId();
		this.orderNo = order.getOrderNo();
		this.customerName = order.getCustomer().getName();
		this.productName = order.getProduct().getName();
		this.quantity = order.getProductCount();
		this.totalPrice = order.getTotalPrice();
		this.createdAt = order.getCreatedAt();
		this.status = order.getStatus();
		this.createdByName = order.getCreatedBy().getName();
	}
}
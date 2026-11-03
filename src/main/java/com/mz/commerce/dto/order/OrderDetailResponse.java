package com.mz.commerce.dto.order;

import java.time.LocalDateTime;

import com.mz.commerce.entity.admin.AdminRole;
import com.mz.commerce.entity.order.OrderStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderDetailResponse {
	private final String orderNo;
	private final String customerName;
	private final String customerEmail;
	private final String productName;
	private final int quantity;
	private final long totalPrice;
	private final LocalDateTime createdAt;
	private final OrderStatus status;
	private final String createdByName;
	private final String createdByEmail;
	private final AdminRole createdByRole;
}
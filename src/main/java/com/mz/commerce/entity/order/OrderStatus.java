package com.mz.commerce.entity.order;

import static com.mz.commerce.common.exception.ErrorCode.*;

import com.mz.commerce.common.exception.CustomException;

public enum OrderStatus {
	PREPARING, DELIVERING, COMPLETED, CANCEL;

	public OrderStatus next() {
		return switch (this) {
			case PREPARING -> DELIVERING;
			case DELIVERING -> COMPLETED;
			default -> throw new CustomException(ORDER_STATUS_NOT_CHANGEABLE);
		};
	}
}
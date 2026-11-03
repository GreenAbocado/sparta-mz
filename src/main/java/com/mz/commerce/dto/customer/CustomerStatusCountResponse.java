package com.mz.commerce.dto.customer;

import com.mz.commerce.entity.customer.CustomerStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomerStatusCountResponse {
	private final CustomerStatus status;
	private final Long count;
}

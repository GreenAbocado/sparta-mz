package com.mz.commerce.dto.customer;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomerDeleteResponse {
	private final Long customerId;
	private final String name;
	private final LocalDateTime deleted;
}

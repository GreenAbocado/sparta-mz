package com.mz.commerce.dto.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RegisteredByResponse {
	private final Long id;
	private final String registeredBy;
	private final String email;
}

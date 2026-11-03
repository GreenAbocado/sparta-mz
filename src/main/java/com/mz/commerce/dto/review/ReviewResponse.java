package com.mz.commerce.dto.review;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewResponse {
	private final Long id;
	private final String orderNo;
	private final String customerName;
	private final String productName;
	private final int rating;
	private final String content;
	private final LocalDateTime createdAt;
}

package com.mz.commerce.dto.review;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewDeleteResponse {
	private final Long id;
	private final LocalDateTime deletedAt;
}

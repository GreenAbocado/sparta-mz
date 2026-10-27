package com.mz.commerce.common;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

@Getter
public class ErrorResponse {
	private final String code;
	private final String message;
	private final LocalDateTime timestamp;

	// DTO, 쿼리 검증 예외 시 반환
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private final List<FieldErrorResponse> fieldError;

	public ErrorResponse(ErrorCode errorCode) {
		this(errorCode, null);
	}

	public ErrorResponse(ErrorCode errorCode, List<FieldErrorResponse> fieldError) {
		this.code = errorCode.getCode();
		this.message = errorCode.getMessage();
		this.timestamp = LocalDateTime.now();
		this.fieldError = fieldError;
	}
}
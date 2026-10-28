package com.mz.commerce.common;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

/**
 * 에러 응답에 사용되는 DTO입니다.
 *
 * <p>{@link GlobalExceptionHandler}에서 {@link ErrorCode}를 매개변수로 생성합니다.
 */
@Getter
public class ErrorResponse {
	private final String code;
	private final String message;
	private final LocalDateTime timestamp;

	/** Bean Validation 실패일 경우만 해당 필드를 포함합니다. */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private final List<FieldErrorResponse> fieldError;

	/**
	 * 에러 코드만을 기반으로 응답 DTO를 생성합니다.
	 *
	 * <p>Bean Validation 검증을 제외한 모든 예외 처리 응답에 사용됩니다.
	 *
	 * @param errorCode 에러 코드 상수.
	 */
	public ErrorResponse(ErrorCode errorCode) {
		this(errorCode, null);
	}

	/**
	 * 에러 코드와 검증 실패 내용을 기반으로 응답 DTO를 생성합니다.
	 *
	 * <p>Bean Validation 실패일 경우 {@link GlobalExceptionHandler}에서 사용합니다.
	 *
	 * @param errorCode 에러 코드 상수.
	 * @param fieldError {@link FieldErrorResponse} 목록. Bean Validation 예외가 아니면 null
	 */
	public ErrorResponse(ErrorCode errorCode, List<FieldErrorResponse> fieldError) {
		this.code = errorCode.getCode();
		this.message = errorCode.getMessage();
		this.timestamp = LocalDateTime.now();
		this.fieldError = fieldError;
	}
}
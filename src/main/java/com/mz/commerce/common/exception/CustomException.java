package com.mz.commerce.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 비즈니스 로직에서 규칙을 위반하였을 때 던지는 예외입니다.
 *
 * <p>{@link GlobalExceptionHandler#handleCustomException}에서
 * 해당 예외를 잡아 {@link ErrorCode}를 꺼내 {@link ErrorResponse}로 변환하여 응답합니다.
 */
@RequiredArgsConstructor
@Getter
public class CustomException extends RuntimeException {
	private final ErrorCode errorCode;
}
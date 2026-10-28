package com.mz.commerce.common;

import org.springframework.validation.FieldError;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;

/**
 * Bean Validation 예외 시 검증 오류 내용을 담는 DTO입니다.
 *
 * <p>API 응답 시 {@link ErrorResponse}에 포함되어 반환됩니다.
 */
@Getter
public class FieldErrorResponse {
	private final String name;
	private final String message;

	/**
	 * DTO 검증 실패 시 FieldErrorResponse를 생성합니다.
	 *
	 * <p>{@link GlobalExceptionHandler#handleValidationException}에서
	 * MethodArgumentNotValidException을 잡아 BindingResult에서 꺼낸 FieldError를 변환합니다.
	 *
	 * @param fieldError BindingResult에서 꺼낸 필드별 검증 실패 정보
	 */
	public FieldErrorResponse(FieldError fieldError) {
		this.name = fieldError.getField();
		this.message = fieldError.getDefaultMessage();
	}

	/**
	 * 쿼리, 경로변수 검증 실패 시 FieldErrorResponse를 생성합니다.
	 *
	 * <p>{@link GlobalExceptionHandler#handleQueryValidationException}에서
	 * ConstraintViolationException을 잡아 넘겨준 ConstraintViolation을 변환합니다.
	 *
	 * @param constraintViolation ConstraintViolationException에서 꺼낸 필드별 검증 실패 정보
	 */
	public FieldErrorResponse(ConstraintViolation<?> constraintViolation) {
		String fullName = constraintViolation.getPropertyPath().toString();
		this.name = fullName.contains(".") ?
			fullName.substring(fullName.lastIndexOf('.') + 1) : fullName;
		this.message = constraintViolation.getMessage();
	}
}
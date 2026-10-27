package com.mz.commerce.common;

import org.springframework.validation.FieldError;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;

@Getter
// 검증 실패 시 ErrorResponse에 포함하여 반환
public class FieldErrorResponse {
	private final String name;
	private final String message;

	// DTO 검증 에외 시
	public FieldErrorResponse(FieldError fieldError) {
		this.name = fieldError.getField();
		this.message = fieldError.getDefaultMessage();
	}

	// 쿼리 검증 예외의 경우 경로(클래스명.파라미터명)에서 파라미터명만 사용
	public FieldErrorResponse(ConstraintViolation<?> constraintViolation) {
		String fullName = constraintViolation.getPropertyPath().toString();
		this.name = fullName.contains(".") ?
			fullName.substring(fullName.lastIndexOf('.') + 1) : fullName;
		this.message = constraintViolation.getMessage();
	}
}
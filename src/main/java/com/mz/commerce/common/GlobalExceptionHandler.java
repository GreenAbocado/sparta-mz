package com.mz.commerce.common;

import static com.mz.commerce.common.ErrorCode.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// HTTP 요청 형식 오류 (JSON 깨짐, 타입 변환 실패 등)
	@ExceptionHandler({HttpMessageNotReadableException.class,
		MethodArgumentTypeMismatchException.class,
		MissingServletRequestParameterException.class})
	public ResponseEntity<ErrorResponse> handleHttpFormatException() {
		return buildResponseEntity(REQUEST_FORMAT_INVALID);
	}

	// DTO 검증 실패
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
		List<FieldErrorResponse> fieldErrors = e.getBindingResult().getFieldErrors().stream()
			.map(FieldErrorResponse::new).toList();

		return buildValidationFailResponseEntity(fieldErrors);
	}

	// 쿼리 or 경로변수 검증 실패
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleQueryValidationException(ConstraintViolationException e) {
		List<FieldErrorResponse> fieldErrors = e.getConstraintViolations().stream()
			.map(FieldErrorResponse::new).toList();

		return buildValidationFailResponseEntity(fieldErrors);
	}

	// 비즈니스 예외
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
		return buildResponseEntity(e.getErrorCode());
	}

	// URL Not Found
	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ErrorResponse> handleNoResourceFoundException() {
		return buildResponseEntity(REQUEST_URL_INVALID);
	}

	// Http 메서드 불일치
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException() {
		return buildResponseEntity(REQUEST_METHOD_INVALID);
	}

	// 클라이언트 오류 발견시 추가 필요

	// 서버 에러
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleAllException() {
		return buildResponseEntity(SERVER_ERROR);
	}

	private ResponseEntity<ErrorResponse> buildResponseEntity(ErrorCode errorCode) {
		return ResponseEntity.status(errorCode.getStatus())
			.body(new ErrorResponse(errorCode));
	}

	private ResponseEntity<ErrorResponse> buildValidationFailResponseEntity(List<FieldErrorResponse> fieldErrors) {
		return ResponseEntity.status(REQUEST_VALIDATION_FAILED.getStatus())
			.body(new ErrorResponse(REQUEST_VALIDATION_FAILED, fieldErrors));
	}
}
package com.mz.commerce.common.exception;

import static com.mz.commerce.common.exception.ErrorCode.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.validation.ConstraintViolationException;

/**
 * 전역 예외 처리를 담당합니다.
 *
 * <p>각 예외를 {@link ErrorCode}에 대응시켜 {@link ErrorResponse}로 변환합니다.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * HTTP 요청 형식 오류를 처리합니다.
	 *
	 * <p>JSON 파싱 오류, 타입 불일치, 쿼리 누락이 해당됩니다.
	 * {@link ErrorCode#REQUEST_FORMAT_INVALID}에 대응됩니다.
	 */
	@ExceptionHandler({HttpMessageNotReadableException.class,
		MethodArgumentTypeMismatchException.class,
		MissingServletRequestParameterException.class})
	public ResponseEntity<ErrorResponse> handleHttpFormatException() {
		return buildResponseEntity(REQUEST_FORMAT_INVALID);
	}

	/**
	 * DTO 검증, ModelAttribute 등 BindingResult 관련 오류를 처리합니다.
	 *
	 * <p>{@link ErrorCode#REQUEST_VALIDATION_FAILED}에 대응됩니다.
	 * {@link MethodArgumentNotValidException#getBindingResult}를 통해 꺼낸 fieldError로
	 * {@link ErrorResponse}를 생성할 때 필드별 에러 내역을 상세히 담습니다.
	 */
	@ExceptionHandler(BindException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(BindException e) {
		List<FieldErrorResponse> fieldErrors = e.getBindingResult().getFieldErrors().stream()
			.map(FieldErrorResponse::new).toList();

		return buildValidationFailResponseEntity(fieldErrors);
	}

	/**
	 * 쿼리, 경로변수 Bean Validation 검증 오류를 처리합니다.
	 *
	 * <p>{@link ErrorCode#REQUEST_VALIDATION_FAILED}에 대응됩니다.
	 * {@link ConstraintViolationException#getConstraintViolations}를 통해 꺼낸 ConstraintViolation으로
	 * {@link ErrorResponse}를 생성할 때 필드별 에러 내역을 상세히 담습니다.
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleQueryValidationException(ConstraintViolationException e) {
		List<FieldErrorResponse> fieldErrors = e.getConstraintViolations().stream()
			.map(FieldErrorResponse::new).toList();

		return buildValidationFailResponseEntity(fieldErrors);
	}

	/**
	 * 비즈니스 예외를 처리합니다.
	 *
	 * <p>{@link ErrorCode}에 각 도메인별로 정의한 상수와 대응됩니다.
	 *
	 * @param e 비즈니스 예외에서 공통으로 사용되는 커스텀 예외
	 */
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
		return buildResponseEntity(e.getErrorCode());
	}

	/**
	 * 잘못된 URL 요청을 처리합니다.
	 *
	 * <p>{@link ErrorCode#REQUEST_URL_INVALID}와 대응됩니다.
	 */
	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ErrorResponse> handleNoResourceFoundException() {
		return buildResponseEntity(REQUEST_URL_INVALID);
	}

	/**
	 * 잘못된 HTTP 메서드 요청을 처리합니다.
	 *
	 * <p>{@link ErrorCode#REQUEST_METHOD_INVALID}와 대응됩니다.
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException() {
		return buildResponseEntity(REQUEST_METHOD_INVALID);
	}

	/**
	 * 서버 내부 오류를 처리합니다.
	 *
	 * <p>위에서 잡히지 않은 클라이언트 오류가 처리될 수 있습니다.
	 * {@link ErrorCode#SERVER_ERROR}와 대응됩니다.
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleAllException() {
		return buildResponseEntity(SERVER_ERROR);
	}

	/**
	 * 예외 처리 핸들러에서 반복되는 ResponseEntity 빌딩 작업을 처리합니다.
	 *
	 * @param errorCode 각 예외 처리 핸들러에서 전달한 에러코드
	 */
	private ResponseEntity<ErrorResponse> buildResponseEntity(ErrorCode errorCode) {
		return ResponseEntity.status(errorCode.getStatus())
			.body(new ErrorResponse(errorCode));
	}

	/**
	 * 검증 예외 처리 핸들러 2개에서 반복되는 빌딩 작업을 처리합니다.
	 *
	 * <p>{@link ErrorCode#REQUEST_VALIDATION_FAILED}와 fieldErrors를 통해 빌딩 작업을 처리합니다.
	 *
	 * @param fieldErrors 검증 예외에서 꺼낸 필드 에러 정보를 변환한 {@link FieldErrorResponse}
	 */
	private ResponseEntity<ErrorResponse> buildValidationFailResponseEntity(List<FieldErrorResponse> fieldErrors) {
		return ResponseEntity.status(REQUEST_VALIDATION_FAILED.getStatus())
			.body(new ErrorResponse(REQUEST_VALIDATION_FAILED, fieldErrors));
	}
}
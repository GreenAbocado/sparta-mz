package com.mz.commerce.common;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 각 도메인 별 에러 관리
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// 공통
	REQUEST_FORMAT_INVALID(HttpStatus.BAD_REQUEST, "G001", "요청 형식이 잘못되었습니다."),
	REQUEST_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "G002", "입력값 검증에 실패하였습니다."),
	REQUEST_URL_INVALID(HttpStatus.NOT_FOUND, "G003", "URL 경로가 잘못되었습니다."),
	REQUEST_METHOD_INVALID(HttpStatus.METHOD_NOT_ALLOWED, "G004", "HTTP 메서드가 잘못되었습니다."),

	SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G500", "서버 내부 오류입니다."),

	// 관리자

	// 회원

	// 상품

	// 주문



	;
	private final HttpStatus status;
	private final String code;
	private final String message;
}

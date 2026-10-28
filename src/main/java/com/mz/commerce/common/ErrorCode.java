package com.mz.commerce.common;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * API 응답에 사용하는 에러 상태를 도메인별로 정의합니다.
 *
 * <p>각 상수는 HTTP 상태, 도메인별 에러코드 문자열, 메시지를 가집니다.
 * 코드 접두사는 도메인을 나타냅니다. (G: 공통)
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// 공통

	/** JSON 문법 오류, 타입 불일치, Query 누락 */
	REQUEST_FORMAT_INVALID(HttpStatus.BAD_REQUEST, "G001", "요청 형식이 잘못되었습니다."),

	/** Bean Validation 실패. DTO, QueryParam, PathVariable */
	REQUEST_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "G002", "입력값 검증에 실패하였습니다."),

	REQUEST_URL_INVALID(HttpStatus.NOT_FOUND, "G003", "URL 경로가 잘못되었습니다."),
	REQUEST_METHOD_INVALID(HttpStatus.METHOD_NOT_ALLOWED, "G004", "HTTP 메서드가 잘못되었습니다."),

	SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G500", "서버 내부 오류입니다."),

	// 회원
	DUPLICATE_EMAIL(HttpStatus.CONFLICT, "A001", "이미 사용 중인 이메일입니다."),
	EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "A002", "존재하지 않는 이메일입니다."),
	INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "A003", "비밀번호가 일치하지 않습니다."),
	ADMIN_NOT_ACTIVE(HttpStatus.FORBIDDEN, "A004", "관리자가 '활성' 상태가 아닙니다."),
	// 상품
	PRODUCT_CATEGORY_INVALID(HttpStatus.BAD_REQUEST, "P001",
		"상품 카테고리가 올바르지 않습니다. (사용 가능한 카테고리: ELECTRONICS, CLOTHING, FOOD)"),
	PRODUCT_REGISTER_STATUS_INVALID(HttpStatus.BAD_REQUEST, "P002",
		"상품 등록 시 직접 선택할 수 있는 상태는 DISCONTINUED뿐입니다. 판매중과 품절은 재고에 따라 자동으로 결정됩니다."),
	PRODUCT_STATUS_INVALID(HttpStatus.BAD_REQUEST, "P003",
		"상품 상태가 올바르지 않습니다. (사용 가능한 상태: ON_SALE, SOLD_OUT, DISCONTINUED)"),
	PRODUCT_SORT_ORDER_INVALID(HttpStatus.BAD_REQUEST, "P004", "정렬 순서는 asc 또는 desc만 가능합니다."),
	PRODUCT_SORT_FIELD_INVALID(HttpStatus.BAD_REQUEST, "P005", "지원하지 않는 정렬 기준입니다. (price, stock, createdAt)"),
	PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "P006", "해당 ID의 상품을 찾을 수 없습니다.");

	// 주문
	;

	/** HTTP 응답 상태 코드 */
	private final HttpStatus status;

	/** 에러 식별 코드. 도메인 접두사 + 번호 (예: G001) */
	private final String code;

	/** 사용자에게 보여줄 메시지 */
	private final String message;
}
package com.mz.commerce.common.exception;

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
	AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "G005", "인증에 실패하였습니다."),
	SESSION_INVALIDATED(HttpStatus.CONFLICT, "G006", "세션이 강제 만료되었습니다. 다시 로그인 해주세요."),
	AUTHORIZED_FORBIDDEN(HttpStatus.FORBIDDEN, "G007", "관리자 인가에 실패하였습니다."),
	SORT_ORDER_INVALID(HttpStatus.BAD_REQUEST, "G008", "정렬 순서는 asc 또는 desc만 가능합니다."),
	SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G500", "서버 내부 오류입니다."),

	// 관리자
	DUPLICATE_EMAIL(HttpStatus.CONFLICT, "A001", "이미 사용 중인 이메일입니다."),
	EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "A002", "존재하지 않는 이메일입니다."),
	INVALID_LOGIN(HttpStatus.UNAUTHORIZED, "A003", "이메일 또는 비밀번호가 일치하지 않습니다."),
	ADMIN_NOT_ACTIVE(HttpStatus.FORBIDDEN, "A004", "관리자가 '활성' 상태가 아닙니다."),
	ADMIN_NOT_FOUND(HttpStatus.NOT_FOUND, "A005", "해당 ID의 관리자를 찾을 수 없습니다."),
	REJECTION_REASON_REQUIRED(HttpStatus.BAD_REQUEST, "A006", "반려 시에는 사유가 필수입니다."),
	ADMIN_ALREADY_ACTIVE(HttpStatus.CONFLICT, "A007", "이미 승인된 관리자입니다."),
	ADMIN_ALREADY_REJECTED(HttpStatus.CONFLICT, "A008", "이미 거부된 관리자입니다."),
	ADMIN_APPROVAL_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "A009", "현재 상태에서는 가입 승인이 불가능합니다."),
	ADMIN_REJECTION_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "A010", "현재 상태에서는 가입 거부가 불가능합니다."),
	WEAK_PASSWORD(HttpStatus.BAD_REQUEST, "A011", "비밀번호는 최소 8자 이상 입력해야 합니다."),
	INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "A012", "비밀번호가 일치하지 않습니다"),
	ADMIN_INACTIVE(HttpStatus.FORBIDDEN, "A013", "관리자가 '비활성' 상태입니다."),
	ADMIN_REJECTED(HttpStatus.FORBIDDEN, "A014", "관리자가 '거부' 상태입니다."),
	ADMIN_PENDING(HttpStatus.FORBIDDEN, "A015", "관리자가 '승인 대기' 상태입니다."),
	ADMIN_SUSPENDED(HttpStatus.FORBIDDEN, "A016", "관리자가 '정지' 상태입니다."),
	WRONG_ACCESS(HttpStatus.BAD_REQUEST, "A017", "잘못된 접근입니다"),
	INVALID_PAGE_REQUEST(HttpStatus.BAD_REQUEST, "A018", "페이지와 페이지 크기는 1 이상이어야 합니다."),
	INVALID_SORT_FIELD(HttpStatus.BAD_REQUEST, "A019", "지원하지 않는 정렬 필드입니다."),
	INVALID_SORT_ORDER(HttpStatus.BAD_REQUEST, "A020", "정렬 순서는 asc 또는 desc여야 합니다."),
	INVALID_ADMIN_ROLE(HttpStatus.BAD_REQUEST, "A021", "슈퍼 관리자로는 가입할 수 없습니다."),
	// 고객
	CUSTOMER_STATUS_INVALID(HttpStatus.BAD_REQUEST, "G009", "유효하지 않은 상태입니다. (ACTIVE, INACTIVE, SUSPENDED)"),
	CUSTOMER_SORT_FIELD_INVALID(HttpStatus.BAD_REQUEST, "C002",
		"지원하지 않는 정렬 기준입니다. (id, name, email, phone, status, createdAt, modifiedAt)"),
	CUSTOMER_NOT_FOUND(HttpStatus.NOT_FOUND, "C003", "해당 ID의 고객 정보를 찾을 수 없습니다."),

	// 상품
	PRODUCT_CATEGORY_INVALID(HttpStatus.BAD_REQUEST, "P001",
		"상품 카테고리가 올바르지 않습니다. (사용 가능한 카테고리: ELECTRONICS, CLOTHING, FOOD)"),
	PRODUCT_REGISTER_STATUS_INVALID(HttpStatus.BAD_REQUEST, "P002",
		"상품 등록 시 직접 선택할 수 있는 상태는 DISCONTINUED뿐입니다. 판매중과 품절은 재고에 따라 자동으로 결정됩니다."),
	PRODUCT_STATUS_INVALID(HttpStatus.BAD_REQUEST, "P003",
		"상품 상태가 올바르지 않습니다. (사용 가능한 상태: ON_SALE, SOLD_OUT, DISCONTINUED)"),
	PRODUCT_SORT_ORDER_INVALID(HttpStatus.BAD_REQUEST, "P004", "정렬 순서는 asc 또는 desc만 가능합니다."),
	PRODUCT_SORT_FIELD_INVALID(HttpStatus.BAD_REQUEST, "P005",
		"지원하지 않는 정렬 기준입니다. (id, name, category, price, stock, status, createdAt, modifiedAt)"),
	PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "P006", "해당 ID의 상품을 찾을 수 없습니다."),
	PRODUCT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "P007", "상품 관리 권한이 없습니다."),

	// 주문
	;

	/** HTTP 응답 상태 코드 */
	private final HttpStatus status;

	/** 에러 식별 코드. 도메인 접두사 + 번호 (예: G001) */
	private final String code;

	/** 사용자에게 보여줄 메시지 */
	private final String message;
}
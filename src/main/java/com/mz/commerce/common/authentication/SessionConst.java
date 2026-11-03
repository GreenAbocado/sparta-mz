package com.mz.commerce.common.authentication;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * 세션/쿠키 관리에 사용되는 상수들을 보관합니다.
 *
 * <p>여려 곳에서 사용되는 세션 상수를 한 곳에서만 관리하여
 * 혹시라도 터질 런타임 에러를 예방합니다.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionConst {
	public static final String JSESSIONID = "JSESSIONID";
	public static final String SESSION_USER = "SESSION_USER";
}
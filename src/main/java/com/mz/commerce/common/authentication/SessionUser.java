package com.mz.commerce.common.authentication;

import com.mz.commerce.entity.admin.AdminRole;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 세션에 저장되는 인증상태 정보입니다.
 *
 * <p>회원가입 또는 로그인 시 해당 객체를 생성하여 세션에 넣어줍니다.
 */
@Getter
@RequiredArgsConstructor
public class SessionUser {
	private final Long id;
	private final AdminRole role;
}
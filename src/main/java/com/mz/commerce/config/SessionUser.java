package com.mz.commerce.config;

import com.mz.commerce.entity.admin.AdminRole;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SessionUser {
	private final Long id;
	private final AdminRole role;
}

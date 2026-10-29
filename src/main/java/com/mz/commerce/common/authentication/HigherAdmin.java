package com.mz.commerce.common.authentication;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.mz.commerce.entity.admin.AdminRole;

/**
 * 관리자 인가가 필요한 핸들러에 남기는 애너테이션입니다. 단순 표식입니다.
 *
 * <p>{@link SuperAdminInterceptor}에서 HandlerMethod를 확인하여 해당 애너테이션이 존재할 경우,
 * 관리자 인가 로직을 수행합니다.
 *
 * <p> 애너테이션을 메서드에 붙여줄 경우, 최소 권한에 해당하는 AdminRole 상수를 넣어주세요.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HigherAdmin {
	AdminRole role() default AdminRole.CS;
}
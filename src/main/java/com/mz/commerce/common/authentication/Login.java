package com.mz.commerce.common.authentication;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 세션에 저장된 {@link SessionUser}를 파라미터에서 가독성 있게 받기 위해 남기는 애너테이션입니다.
 *
 * <p>@Login이 붙을 경우 ArgumentResolver 중 {@link LoginArgumentResolver}와 매핑되어 처리됩니다.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Login {
}

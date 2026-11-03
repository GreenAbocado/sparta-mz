package com.mz.commerce.common.authentication;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.mz.commerce.common.exception.CustomException;
import com.mz.commerce.common.exception.ErrorCode;
import com.mz.commerce.entity.admin.AdminRole;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 관리자 인가를 위해 필요한 인터셉터입니다.
 *
 * <p>{@link com.mz.commerce.config.WebConfig#addInterceptors}를 통해
 * 스프링 구동 시점 해당 인터셉터를 레지스트리에 등록합니다.
 *
 * <p>인가 전용 API에서만 실행되도록 {@link RequiredRole}이 붙은 핸들러만 적용해서 처리합니다.
 */
public class AdminInterceptor implements HandlerInterceptor {

	/**
	 * 관리자 인가가 필요한 핸들러인지 판단 후 인가 검증 로직을 수행합니다.
	 *
	 * <p>handler 객체를 통해 {@link RequiredRole}이 붙었는지 확인 후 로직을 수행합니다.
	 *
	 * <p>인증 인터셉터에서 세션 및 인증 상태를 확인하였으므로 인증 여부 확인은 불필요합니다.
	 *
	 * @param handler 해당 요청을 처리할 메서드의 정보를 담은 객체입니다.
	 * @throws CustomException 관리자 인가에 실패할 경우 예외를 던집니다.
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		Exception {

		// !정적 리소스 등의 요청일 경우, HandlerMethod가 아니므로 넘어갑니다
		if (!(handler instanceof HandlerMethod handlerMethod)) {
			return true;
		}

		// HandlerMethod를 리플렉션으로 조회하여 애너테이션을 확인합니다.
		RequiredRole roleArr = handlerMethod.getMethodAnnotation(RequiredRole.class);
		if (roleArr == null) {
			return true;
		}

		AdminRole sessionRole = ((SessionUser)request.getSession(false)
			.getAttribute(SessionConst.SESSION_USER)).getRole();

		// 허용목록에 세션에 등록된 Role이 있을 경우 true를 반환합니다.
		for (AdminRole role : roleArr.value()) {
			if (role == sessionRole) {
				return true;
			}
		}

		throw new CustomException(ErrorCode.AUTHORIZED_FORBIDDEN);
	}
}
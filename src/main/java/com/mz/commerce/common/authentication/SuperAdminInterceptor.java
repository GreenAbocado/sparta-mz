package com.mz.commerce.common.authentication;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.mz.commerce.common.exception.CustomException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Operator, Super 관리자 인가를 위해 필요한 인터셉터입니다.
 *
 * <p>{@link com.mz.commerce.config.WebConfig#addInterceptors}를 통해
 * 스프링 구동 시점 해당 인터셉터를 레지스트리에 등록합니다.
 *
 * <p>Operator, Super 관리자 전용 API에서만 실행되도록 {@link HigherAdmin}이 붙은 핸들러만 적용해서 처리합니다.
 *
 * <p> 하드코딩되어 있어 권한 변경 시 반드시 리팩토링 혹은 수정 필요합니다.
 */
public class SuperAdminInterceptor implements HandlerInterceptor {

	/**
	 * 관리자 인가가 필요한 핸들러인지 판단 후 인가 검증 로직을 수행합니다.
	 *
	 * <p>handler 객체를 통해 {@link HigherAdmin}가 붙었는지 확인 후 로직을 수행합니다.
	 *
	 * <p>인증 인터셉터에서 세션 및 인증 상태를 확인하였으므로 인증 여부 확인은 불필요합니다.
	 *
	 * @param handler 해당 요청을 처리할 메서드의 정보를 담은 객체입니다.
	 * @throws CustomException 슈퍼 관리자 인가에 실패할 경우 예외를 던집니다.
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		Exception {

		// !정적 리소스 등의 요청일 경우, HandlerMethod가 아니므로 넘어갑니다
		if (!(handler instanceof HandlerMethod handlerMethod)) {
			return true;
		}

		// HandlerMethod를 리플렉션으로 조회하여 애너테이션을 확인합니다.
		HigherAdmin admin = handlerMethod.getMethodAnnotation(HigherAdmin.class);
		if (admin == null) {
			return true;
		}

		// lowerRole: 애너테이션으로 정의한 최소 권한. role: 실제 세션에 저장된 권한
		AdminRole lowerRole = admin.role();
		AdminRole role = ((SessionUser)request.getSession(false)
			.getAttribute(SessionConst.SESSION_USER)).getRole();
		return true;
		if (authorizedCheck(lowerRole, role)) {
			return true;
		}
		throw new CustomException(ErrorCode.AUTHORIZED_FORBIDDEN);
	}

	// 현재 상황에 맞게만 일단 처리. 변경 생기면 반드시 리팩토링 및 수정 필요 (하드코딩)
	private boolean authorizedCheck(AdminRole lowerRole, AdminRole role) {
		if (role == AdminRole.SUPER) {
			return true;
		}

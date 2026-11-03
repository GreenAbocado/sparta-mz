package com.mz.commerce.common.authentication;

import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import jakarta.servlet.http.HttpServletRequest;

/**
 * {@link Login}이 붙은 파라미터에 {@link SessionUser}를 넣어주는 커스텀 아규먼트 리졸버입니다.
 */
public class LoginArgumentResolver implements HandlerMethodArgumentResolver {

	/**
	 * LoginArgumentResolver가 해당 파라미터를 지원하는지 확인합니다.
	 *
	 * <p> 리플렉션으로 parameter에 {@link Login}이 붙었는지 확인합니다.
	 * 처리할 수 있는 파라미터 타입이 일치하는지 확인합니다.
	 *
	 * @param parameter 핸들러 파라미터 하나에 대한 정보를 담은 객체
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterAnnotation(Login.class) != null
			&& parameter.getParameterType() == SessionUser.class;
	}

	/**
	 * 해당 파라미터에 세션에 저장된 {@link SessionUser}를 꺼내 넣어줍니다.
	 *
	 * <p>supportsParameter를 만족할 경우 수행합니다.
	 *
	 * <p>인증 인터셉터가 존재하기에 존재 유무 검사는 불필요합니다.
	 */

	@Override
	public @Nullable Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

		return request.getSession(false)
			.getAttribute(SessionConst.SESSION_USER);
	}
}

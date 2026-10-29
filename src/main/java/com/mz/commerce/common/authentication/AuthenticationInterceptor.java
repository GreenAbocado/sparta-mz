package com.mz.commerce.common.authentication;

import static com.mz.commerce.common.authentication.SessionConst.*;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.mz.commerce.common.exception.CustomException;
import com.mz.commerce.common.exception.ErrorCode;
import com.mz.commerce.repository.AdminRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/**
 * 인증 공통 처리를 위한 인터셉터입니다.
 *
 * <p>{@link com.mz.commerce.config.WebConfig#addInterceptors}를 통해
 * 스프링 구동 시점 해당 인터셉터를 레지스트리에 등록합니다.
 *
 * <p>과제 요구사항에 따라, 회원가입/로그인을 제외한 모든 API가 인증이 필요하므로
 * 해당 인터셉터를 거치도록 레지스트리 등록 시점에 "/**"를 add로 "/admins/signup", "/admins/login"을 exclude로 설정합니다.
 */
@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

	/** DB와 최신화 비교를 위해 필요합니다. */
	private final AdminRepository adminRepository;

	/**
	 * 각 핸들러로 전달 전, 사용자를 인증하고 DB와 최신화가 맞는지 비교합니다.
	 *
	 * <p>슈퍼관리자에 의한 권한 변경, 삭제 등을 고려하여 요청마다 repository를 통한 비교 작업 또한 수행합니다.
	 * 만일 DB와 세션이 불일치한다면 쿠키/세션 만료를 통해 사용자가 재로그인 하도록 유도합니다.
	 *
	 * @throws CustomException 로그인을 안 했을 경우 등 세션이 존재하지 않거나 해당 세션에 인증 정보가 없을 경우 던집니다.
	 * @throws CustomException 세션과 그 안에 인증 정보가 존재하나 DB와 불일치하는 경우 던집니다.
	 * @return true 인증 및 DB 일치 확인이 문제 없을 경우, 다음 인터셉터 혹은 핸들러로 이동합니다.
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		Exception {

		// 컨트롤러 메서드를 사용하는 것이 아닐 경우 인증 실패로 처리되지 않고 URL Not Found 등으로 처리되게 수정
		if (!(handler instanceof HandlerMethod))
			return true;

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute(SESSION_USER) == null) {
			throw new CustomException(ErrorCode.AUTHENTICATION_FAILED);
		}

		SessionUser sessionUser = (SessionUser)session.getAttribute(SESSION_USER);
		if (!adminRepository.existsByIdAndRoleAndDeletedIsNull(sessionUser.getId(), sessionUser.getRole())) {
			session.invalidate();
			Cookie cookie = new Cookie(JSESSIONID, null);
			cookie.setMaxAge(0);
			cookie.setPath("/");
			response.addCookie(cookie);

			throw new CustomException(ErrorCode.SESSION_INVALIDATED);
		}
		return true;
	}
}

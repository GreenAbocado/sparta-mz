package com.mz.commerce.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.mz.commerce.common.authentication.AuthenticationInterceptor;
import com.mz.commerce.common.authentication.LoginArgumentResolver;
import com.mz.commerce.common.authentication.SuperAdminInterceptor;

import lombok.RequiredArgsConstructor;

/**
 * 스프링 MVC 설정을 담당합니다.
 *
 * <p>지금은 Interceptor와 ArgumentResolver를 등록합니다.
 *
 * <p>{@link AuthenticationInterceptor}의 경우 repository 빈 주입이 필요하여
 * 깔끔하게 해당 객체도 빈으로 띄워 WebConfig에 주입합니다.
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final AuthenticationInterceptor authenticationInterceptor;

	/**
	 * 스프링 구동 시점에 레지스트리에 인터셉터를 등록합니다.
	 *
	 * <p>인증 인터셉터의 경우 회원가입, 로그인을 배제하였습니다.
	 * <p>슈퍼 관리자 인가 인터셉터의 경우 인터셉터 내에서
	 * 애너테이션 기반 리플렉션으로 확인하므로 전체 경로로 열었습니다.
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authenticationInterceptor)
			.addPathPatterns("/**")
			.excludePathPatterns("/admins/register", "/admins/login")
			.order(1);

		registry.addInterceptor(new SuperAdminInterceptor())
			.addPathPatterns("/**")
			.order(2);
	}

	/**
	 * 스프링 구동 시점에 리졸버 목록에 {@link LoginArgumentResolver}를 등록합니다.
	 *
	 * <p>@Login이 붙은 파라미터가 SessionUser를 받는다면 해당 아규먼트 리졸버가 실행됩니다.
	 */

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new LoginArgumentResolver());
	}
}

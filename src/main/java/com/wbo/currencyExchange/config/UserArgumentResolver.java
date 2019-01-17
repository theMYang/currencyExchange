package com.wbo.currencyExchange.config;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.wbo.currencyExchange.domain.UserLogin;
import com.wbo.currencyExchange.service.UserService.UserLoginService;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

	@Autowired
	UserLoginService userLoginService;
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> clazz = parameter.getParameterType();
		return clazz==UserLogin.class;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
		
		String token = getCookie(request, userLoginService.COOKIE_NAME_TOKEN);
		if(token.isEmpty()) {
			return null;
		}
		UserLogin user = userLoginService.getByToken(response, token);
		return user;
	}
	
	public String getCookie(HttpServletRequest request, String token) {
		Cookie[] cookies = request.getCookies();
		for(Cookie cookie : cookies) {
			if(cookie.getName().equals(token))
				return cookie.getValue();
		}
		return null;
	}

}

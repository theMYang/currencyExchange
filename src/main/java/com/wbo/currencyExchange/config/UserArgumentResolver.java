package com.wbo.currencyExchange.config;

import java.util.Map;

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
import com.wbo.currencyExchange.exception.GlobalException;
import com.wbo.currencyExchange.result.CodeMsg;
import com.wbo.currencyExchange.service.userService.UserLoginService;

/**
 * 每次不用检查cookie，直接将已有cookie的用户解析为用户对象
 * @author meiYng
 *
 */
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
		//没有cookie时，如果传入了userLogin对象的变量名，无法像默认对参数解析那样解析对象
		if(token==null || token.isEmpty()) {
			throw new GlobalException(CodeMsg.UNLOGIN_ERROR);
		}
		UserLogin user = userLoginService.getByToken(response, token);
		return user;
	}
	
	public String getCookie(HttpServletRequest request, String token) {
		Cookie[] cookies = request.getCookies();
		if(cookies==null) {
			return null;
		}
		
		for(Cookie cookie : cookies) {
			if(cookie!=null && cookie.getName().equals(token))
				return cookie.getValue();
		}
		return null;
	}

}

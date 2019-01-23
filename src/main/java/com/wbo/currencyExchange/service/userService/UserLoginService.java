package com.wbo.currencyExchange.service.userService;

import javax.servlet.http.HttpServletResponse;

import com.wbo.currencyExchange.domain.UserLogin;
import com.wbo.currencyExchange.result.CodeMsg;

public interface UserLoginService {

	public static final String COOKIE_NAME_TOKEN = "token";
	
	public CodeMsg getUserLogin(HttpServletResponse response, String mobilePhone, String password) ;
	
	public UserLogin getByToken(HttpServletResponse response, String token) ;
	
}

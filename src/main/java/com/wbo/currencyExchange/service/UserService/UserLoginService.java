package com.wbo.currencyExchange.service.UserService;

import javax.servlet.http.HttpServletResponse;

import com.wbo.currencyExchange.domain.UserLogin;
import com.wbo.currencyExchange.result.CodeMsg;

public interface UserLoginService {

	public static final String COOKIE_NAME_TOKEN = "token";
	
	public CodeMsg getUserLogin(HttpServletResponse response, UserLogin userLogin) ;
	
	public UserLogin getByToken(HttpServletResponse response, String token) ;
	
}

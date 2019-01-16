package com.wbo.currencyExchange.service.UserService;

import javax.servlet.http.HttpServletResponse;

import com.wbo.currencyExchange.domain.UserLogin;
import com.wbo.currencyExchange.result.CodeMsg;

public interface UserLoginService {

	public CodeMsg getUserLoginById(HttpServletResponse response, UserLogin userLogin) ;
}

package com.wbo.currencyExchange.controller.UserController;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wbo.currencyExchange.domain.UserLogin;
import com.wbo.currencyExchange.redis.RedisService;
import com.wbo.currencyExchange.redis.UserKey;
import com.wbo.currencyExchange.result.CodeMsg;
import com.wbo.currencyExchange.result.ResultCode;
import com.wbo.currencyExchange.service.UserService.UserLoginService;
import com.wbo.currencyExchange.service.UserService.UserRegisterService;
import com.wbo.currencyExchange.serviceImpl.UserServiceImpl.UserLoginServiceImpl;

@Controller
@RequestMapping("/login")
public class UserLoginController {

	@Autowired
	UserRegisterService userRegisterService;
	@Autowired
	UserLoginService userLoginService;
	
	@RequestMapping("/register")
	@ResponseBody
	public ResultCode<Integer> doRegister(UserLogin userRegister) {
		int userLoginId = userRegisterService.insertUserLogin(userRegister);
    	return ResultCode.success(userLoginId);
	}
	
	@RequestMapping("/doLogin")
	@ResponseBody
	public ResultCode<Boolean> doLogin(HttpServletResponse response, UserLogin userLogin){
		CodeMsg loginCodeMsg = userLoginService.getUserLogin(response, userLogin);
		if(loginCodeMsg.getCode() == 0) {
			return ResultCode.success(true);
		}else {
			return ResultCode.error(loginCodeMsg);
		}
	}
	
}

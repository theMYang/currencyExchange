package com.wbo.currencyExchange.controller.userController;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wbo.currencyExchange.domain.UserLogin;
import com.wbo.currencyExchange.result.CodeMsg;
import com.wbo.currencyExchange.result.ResultCode;
import com.wbo.currencyExchange.service.userService.UserLoginService;
import com.wbo.currencyExchange.service.userService.UserRegisterService;

@Controller
@RequestMapping("/login")
public class UserLoginController {

	@Autowired
	UserRegisterService userRegisterService;
	@Autowired
	UserLoginService userLoginService;
	
	@RequestMapping("/register")
	@ResponseBody
	public ResultCode<Integer> doRegister(String loginName, String password, String mobilePhone) {
		UserLogin user = new UserLogin(loginName, password, mobilePhone);
		int userId = userRegisterService.insertUserLogin(user);
    	return ResultCode.success(userId);
	}
	
	@RequestMapping("/doLogin")
	@ResponseBody
	public ResultCode<Boolean> doLogin(HttpServletResponse response, String password, String mobilePhone){
		CodeMsg loginCodeMsg = userLoginService.getUserLogin(response, mobilePhone, password);
		if(loginCodeMsg.getCode() == 0) {
			return ResultCode.success(true);
		}else {
			return ResultCode.error(loginCodeMsg);
		}
	}
	
}

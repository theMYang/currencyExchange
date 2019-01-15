package com.wbo.currencyExchange.controller.UserController;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wbo.currencyExchange.domain.UserLogin;
import com.wbo.currencyExchange.result.ResultCode;
import com.wbo.currencyExchange.service.UserService.UserRegisterService;

@Controller
@RequestMapping("/register")
public class UserRegisterController {

	@Autowired
	UserRegisterService userRegisterService;
	
	@RequestMapping("/doRegister")
	@ResponseBody
	public ResultCode<Integer> doLogin(UserLogin user) {
		int userLoginId = userRegisterService.insertUserLogin(user);
    	return ResultCode.success(userLoginId);
	}
	
	@RequestMapping("/testRegister")
	@ResponseBody
	public ResultCode<String> test() {
    	return ResultCode.success("testRegisterq");
	}
}

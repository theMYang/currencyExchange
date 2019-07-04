package com.wbo.currencyExchange.controller.digitalCurrencyController;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.wbo.currencyExchange.domain.DigitalCurrency;
import com.wbo.currencyExchange.domain.UserLogin;
import com.wbo.currencyExchange.result.ResultCode;
import com.wbo.currencyExchange.service.digitalCurrencyService.DigitalCurrencyService;
import com.wbo.currencyExchange.service.userService.UserLoginService;

@Controller
@RequestMapping("/market")
public class DigitalCurrencyController {

	@Autowired
	DigitalCurrencyService digitalCurrencyService;
	@Autowired
	UserLoginService userLoginService;
	
	@RequestMapping("/home")
	public String getHomePageCurrency() {
		List<DigitalCurrency> homePageCurrency = digitalCurrencyService.getHomePageCurrency();
		ResultCode<List<DigitalCurrency>> homePageCurrencyRet = ResultCode.success(homePageCurrency);
		String homePageCurrencyJson = JSON.toJSONString(homePageCurrencyRet);
		return homePageCurrencyJson;
	}
	
	@RequestMapping("/exchange")
	@ResponseBody
	public ResultCode<UserLogin> initMarket(UserLogin user) {
		return ResultCode.success(user);
	}
	
	
}

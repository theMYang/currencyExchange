package com.wbo.currencyExchange.controller.DigitalCurrencyController;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.wbo.currencyExchange.domain.DigitalCurrency;
import com.wbo.currencyExchange.domain.UserLogin;
import com.wbo.currencyExchange.result.ResultCode;
import com.wbo.currencyExchange.service.DigitalCurrencyService.DigitalCurrencyService;
import com.wbo.currencyExchange.service.UserService.UserLoginService;
import com.wbo.currencyExchange.serviceImpl.UserServiceImpl.UserLoginServiceImpl;

@Controller
@RequestMapping("/market")
public class DigitalCurrencyController {

	@Autowired
	DigitalCurrencyService digitalCurrencyService;
	@Autowired
	UserLoginService userLoginService;
	
	
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

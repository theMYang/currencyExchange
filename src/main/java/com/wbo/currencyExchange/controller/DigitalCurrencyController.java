package com.wbo.currencyExchange.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.wbo.currencyExchange.domain.DigitalCurrency;
import com.wbo.currencyExchange.result.ResultCode;
import com.wbo.currencyExchange.service.DigitalCurrencyService;

@Controller
public class DigitalCurrencyController {

	@Autowired
	DigitalCurrencyService digitalCurrencyService;
	
	
	public String getHomePageCurrency() {
		List<DigitalCurrency> homePageCurrency = digitalCurrencyService.getHomePageCurrency();
		ResultCode<List<DigitalCurrency>> homePageCurrencyRet = ResultCode.success(homePageCurrency);
		String homePageCurrencyJson = JSON.toJSONString(homePageCurrencyRet);
		return homePageCurrencyJson;
	}
}

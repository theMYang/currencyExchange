package com.wbo.currencyExchange;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.wbo.currencyExchange.controller.DigitalCurrencyController.DigitalCurrencyController;
import com.wbo.currencyExchange.result.ResultCode;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DigitalCurrencyTest {
	
	private static Logger logger = LogManager.getLogger(DigitalCurrencyTest.class);

	@Autowired
	DigitalCurrencyController digitalCurrencyController;
	
	@Test
	public void testHomePageCurrency() {
		String testResult = digitalCurrencyController.getHomePageCurrency();
		logger.info("retJSON"+ testResult);
	}
	
}

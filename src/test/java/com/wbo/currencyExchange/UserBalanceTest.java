package com.wbo.currencyExchange;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.wbo.currencyExchange.domain.UserBalance;
import com.wbo.currencyExchange.service.userService.UserBalanceService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserBalanceTest {
	
	private static Logger logger = LogManager.getLogger(UserBalanceTest.class);
	
	@Autowired
	UserBalanceService userBalanceService;
	@Autowired
	UserBalance userBalance;
	
	@Test
	public void freezeBalanceForOrderDB() {
		userBalance.setUserId(7);
		Double amount = 110000.12345;
		userBalance.setFreezeAmount(new BigDecimal(amount));
		boolean res = userBalanceService.freezeBalanceForOrderDB(userBalance);
		logger.info("返回值："+res);
	}
}

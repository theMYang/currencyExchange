package com.wbo.currencyExchange;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.wbo.currencyExchange.domain.UserAsset;
import com.wbo.currencyExchange.service.userService.UserAssetService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserAssetTest {

	private static Logger logger = LogManager.getLogger(UserAssetTest.class);
	
	@Autowired
	UserAssetService userAssetService;
	
	@Test
	public void UserAssetTest() {
		UserAsset userAsset = userAssetService.getUserAssetByUserId(7, 102);
		System.err.println(userAsset);
	}
}

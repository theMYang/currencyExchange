package com.wbo.currencyExchange;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.wbo.currencyExchange.domain.UserLogin;
import com.wbo.currencyExchange.service.orderService.PlaceOrderService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlaceOrderTest {

	@Autowired
	UserLogin user;
	@Autowired
	PlaceOrderService placeOrderService;
	
	@Test
	public void placeBuyOrder() {
		
		BigDecimal purchaseAmount = new BigDecimal(100);
		BigDecimal purchasePrice = new BigDecimal(20);
		user.setUserId(7);
		placeOrderService.placeBuyOrder(purchaseAmount, purchasePrice, 101, user);
		
		
	}
	
	
}

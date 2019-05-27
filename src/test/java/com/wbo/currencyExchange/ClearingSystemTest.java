package com.wbo.currencyExchange;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.wbo.currencyExchange.domain.Deal;
import com.wbo.currencyExchange.domain.Order;
import com.wbo.currencyExchange.exception.GlobalException;
import com.wbo.currencyExchange.rabbitMQ.producer.DefaultMqSender;
import com.wbo.currencyExchange.rabbitMQ.producer.MatchedOrderMqSendEnvelop;
import com.wbo.currencyExchange.result.CodeMsg;
import com.wbo.currencyExchange.service.clearingService.ClearingSystemService;
import com.wbo.currencyExchange.service.dealService.DealService;
import com.wbo.currencyExchange.service.orderService.PlaceOrderService;
import com.wbo.currencyExchange.serviceImpl.clearingServiceImpl.ClearingSystemServiceImpl;


@RunWith(SpringRunner.class)
@SpringBootTest
@Component
public class ClearingSystemTest {

	@Autowired
	ClearingSystemService clearingSystemService;
	@Autowired
	DefaultMqSender mqSenderForMatchedOrder;
	@Autowired
	PlaceOrderService placeOrderService;
	@Autowired
	ClearingSystemServiceImpl clearingSystemServiceImpl;
	@Autowired
	Deal deal;
	
	@Autowired
	DealService dealService;
	
	@Test
	public void clearingOrderTest() {
		Order orderA = new Order();
		Order orderB = new Order();
		
		orderA.setUserId(7);
		orderB.setUserId(8);
		
		orderA.setCurrencyId(101);
		orderB.setCurrencyId(101);
		
		orderA.setOrderId(Long.parseLong("675282944"));
		orderB.setOrderId(Long.parseLong("981467136"));
		
		
		orderA.setOrderAmount(new BigDecimal(100));
		orderB.setOrderAmount(new BigDecimal(100));
		
		orderA.setOrderPrice(new BigDecimal(20));
		orderB.setOrderPrice(new BigDecimal(20));
		
		orderA.setDealAmount(new BigDecimal(0));
		orderB.setDealAmount(new BigDecimal(0));
		
		orderA.setDealPrice(new BigDecimal(0));
		orderB.setDealPrice(new BigDecimal(0));
		
		orderA.setEndStateTime(new Timestamp(System.currentTimeMillis()));
		orderB.setEndStateTime(new Timestamp(System.currentTimeMillis()));
		
		
		clearingSystemServiceImpl.clearingOrder(orderA, orderB);
		
//		System.err.println("更新了几条："+num);
		
		
	}
	
	
	
}

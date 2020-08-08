package com.wbo.currencyExchange;

import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import com.wbo.currencyExchange.domain.MatchSequence;
import com.wbo.currencyExchange.domain.Order;
import com.wbo.currencyExchange.domain.OrderDriven;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Testa {

	@Autowired
	Order order1;
	@Autowired
	Order order2;
	
	@Test
	public void test1() {
		order1.setCurrencyId(1010);
		order2.setCurrencyId(1050);
		System.err.println(order1);
		System.err.println(order2);
	}
}

package com.wbo.currencyExchange;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wbo.currencyExchange.annotation.ValidMatchedOrder;
import com.wbo.currencyExchange.domain.MatchSequence;
import com.wbo.currencyExchange.domain.Order;
import com.wbo.currencyExchange.domain.OrderDriven;

@Service

public class Testb {

	@Autowired
	MatchSequence matchSequence;
	
	public void test1() {
		ConcurrentHashMap<Integer, OrderDriven>	map = matchSequence.getSequenceMap();
		map.put(456654, new OrderDriven());
//		matchSequence.setSequenceMap(map);
	}
	
	@ValidMatchedOrder
	public void testAnnotation(Order buyOrder, Order sellOrder) {
		
		System.err.println("done");
	}
}

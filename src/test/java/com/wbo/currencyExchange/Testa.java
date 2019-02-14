package com.wbo.currencyExchange;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wbo.currencyExchange.domain.MatchSequence;
import com.wbo.currencyExchange.domain.Order;
import com.wbo.currencyExchange.domain.OrderDriven;

@Service
public class Testa {

	@Autowired
	MatchSequence matchSequence;
	
	public void test1() {
		ConcurrentHashMap<Integer, OrderDriven>	map = matchSequence.getSequenceMap();
		map.put(123321, new OrderDriven());
//		matchSequence.setSequenceMap(map);
	}
}

package com.wbo.currencyExchange.service.matchService;

import com.wbo.currencyExchange.domain.Order;

public interface SequenceService {

	// 订单进队列
	public void placeOrderToSequence(Order order);
	
}

package com.wbo.currencyExchange.service.matchService;

import java.util.List;

import com.wbo.currencyExchange.domain.Order;

public interface MatchEngineService {

	public void initMatchEngine();
	
	public void sendMatchedOrder(List<Order> orderGroup);

}

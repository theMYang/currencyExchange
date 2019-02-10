package com.wbo.currencyExchange.domain;

import java.util.concurrent.ConcurrentSkipListSet;

// 买卖盘
public class OrderDriven {

	private ConcurrentSkipListSet<Order> buySet;
	private ConcurrentSkipListSet<Order> sellSet;
	
	
	public ConcurrentSkipListSet<Order> getBuySet() {
		return buySet;
	}
	public void setBuySet(ConcurrentSkipListSet<Order> buySet) {
		this.buySet = buySet;
	}
	public ConcurrentSkipListSet<Order> getSellSet() {
		return sellSet;
	}
	public void setSellSet(ConcurrentSkipListSet<Order> sellSet) {
		this.sellSet = sellSet;
	}
	
	
	
}

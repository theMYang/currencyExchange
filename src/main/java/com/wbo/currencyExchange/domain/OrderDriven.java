package com.wbo.currencyExchange.domain;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

// 买卖盘
public class OrderDriven {

	private ConcurrentSkipListSet<Order> buyOrderDriven;
	private ConcurrentSkipListSet<Order> sellOrderDriven;
	
	public static final int BUY_ORDER_TYPE = 1;
	public static final int SELL_ORDER_TYPE = 2;
	
	
	public OrderDriven() {	}
	
	public OrderDriven(Collection<Order> buyC, Collection<Order> sellC) {
		orderDrivenComparator orderDrivenBuyComparator = new orderDrivenComparator(BUY_ORDER_TYPE);
		this.buyOrderDriven = new ConcurrentSkipListSet<>(orderDrivenBuyComparator);
		this.buyOrderDriven.addAll(buyC);
		
		orderDrivenComparator orderDrivenSellComparator = new orderDrivenComparator(BUY_ORDER_TYPE);
		this.sellOrderDriven = new ConcurrentSkipListSet<>(orderDrivenSellComparator);
		this.sellOrderDriven.addAll(sellC);
	}
	
	public OrderDriven(int orderCompareType, Collection<Order> orderC) {
		if(orderCompareType == BUY_ORDER_TYPE) {
			orderDrivenComparator orderDrivenBuyComparator = new orderDrivenComparator(BUY_ORDER_TYPE);
			this.buyOrderDriven = new ConcurrentSkipListSet<>(orderDrivenBuyComparator);
			this.buyOrderDriven.addAll(orderC);
			
			orderDrivenComparator orderDrivenSellComparator = new orderDrivenComparator(BUY_ORDER_TYPE);
			this.sellOrderDriven = new ConcurrentSkipListSet<>(orderDrivenSellComparator);
			
		}else if(orderCompareType == SELL_ORDER_TYPE) {
			orderDrivenComparator orderDrivenBuyComparator = new orderDrivenComparator(BUY_ORDER_TYPE);
			this.buyOrderDriven = new ConcurrentSkipListSet<>(orderDrivenBuyComparator);
			
			orderDrivenComparator orderDrivenSellComparator = new orderDrivenComparator(BUY_ORDER_TYPE);
			this.sellOrderDriven = new ConcurrentSkipListSet<>(orderDrivenSellComparator);
			this.sellOrderDriven.addAll(orderC);
		}
	}
	
	
	
	private final class orderDrivenComparator implements Comparator<Order> {

		private int orderCompareType;
		
		public orderDrivenComparator(int orderCompareType) {
			this.orderCompareType = orderCompareType;
		}
		
		@Override
		public int compare(Order o1, Order o2) {
			int compareRes = 0;
			if(orderCompareType == BUY_ORDER_TYPE) {
				compareRes = o2.getOrderPrice().compareTo(o1.getOrderPrice());
			}else if(orderCompareType == SELL_ORDER_TYPE) {
				compareRes = o1.getOrderPrice().compareTo(o2.getOrderPrice());
			}
			
			compareRes = compareRes ==0?  o1.getOrderCreateTime().compareTo(o2.getOrderCreateTime()): compareRes;
			compareRes = compareRes ==0?  o2.getOrderAmount().compareTo(o1.getOrderAmount()): compareRes;
			compareRes = compareRes ==0?  o1.getOrderId().compareTo(o2.getOrderId()): compareRes;
			return compareRes;
		}

	}
	
	
	public ConcurrentSkipListSet<Order> getBuyOrderDriven() {
		return buyOrderDriven;
	}
	public void setBuyOrderDriven(ConcurrentSkipListSet<Order> buyOrderDriven) {
		this.buyOrderDriven = buyOrderDriven;
	}
	public void setBuyOrderDriven(Collection<Order> buyOrderDriven) {
		this.buyOrderDriven.addAll(buyOrderDriven);
	}
	public ConcurrentSkipListSet<Order> getSellOrderDriven() {
		return sellOrderDriven;
	}
	public void setSellOrderDriven(ConcurrentSkipListSet<Order> sellOrderDriven) {
		this.sellOrderDriven = sellOrderDriven;
	}
	public void setSellOrderDriven(Collection<Order> sellOrderDriven) {
		this.sellOrderDriven.addAll(sellOrderDriven);
	}
	

	@Override
	public String toString() {
		return "OrderDriven {buyOrderDriven=" + buyOrderDriven + ", sellOrderDriven=" + sellOrderDriven + "}";
	}
	
	
}

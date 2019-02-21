package com.wbo.currencyExchange.domain;

import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.ConcurrentSkipListSet;

import org.springframework.stereotype.Component;

// 买卖盘
@Component
public class OrderDriven {

	private ConcurrentSkipListSet<Order> buyOrderDriven;
	private ConcurrentSkipListSet<Order> sellOrderDriven;
	
	
	public OrderDriven() {
		OrderDrivenComparator orderDrivenBuyComparator = new OrderDrivenComparator(Order.BUY_ORDER_TYPE);
		this.buyOrderDriven = new ConcurrentSkipListSet<>(orderDrivenBuyComparator);
		
		OrderDrivenComparator orderDrivenSellComparator = new OrderDrivenComparator(Order.SELL_ORDER_TYPE);
		this.sellOrderDriven = new ConcurrentSkipListSet<>(orderDrivenSellComparator);
	}
	
	public OrderDriven(Collection<Order> buyC, Collection<Order> sellC) {
		OrderDrivenComparator orderDrivenBuyComparator = new OrderDrivenComparator(Order.BUY_ORDER_TYPE);
		this.buyOrderDriven = new ConcurrentSkipListSet<>(orderDrivenBuyComparator);
		this.buyOrderDriven.addAll(buyC);
		
		OrderDrivenComparator orderDrivenSellComparator = new OrderDrivenComparator(Order.SELL_ORDER_TYPE);
		this.sellOrderDriven = new ConcurrentSkipListSet<>(orderDrivenSellComparator);
		this.sellOrderDriven.addAll(sellC);
	}
	
	public OrderDriven(int orderCompareType, Collection<Order> orderC) {
		if(orderCompareType == Order.BUY_ORDER_TYPE) {
			OrderDrivenComparator orderDrivenBuyComparator = new OrderDrivenComparator(Order.BUY_ORDER_TYPE);
			this.buyOrderDriven = new ConcurrentSkipListSet<>(orderDrivenBuyComparator);
			this.buyOrderDriven.addAll(orderC);
			
			OrderDrivenComparator orderDrivenSellComparator = new OrderDrivenComparator(Order.SELL_ORDER_TYPE);
			this.sellOrderDriven = new ConcurrentSkipListSet<>(orderDrivenSellComparator);
			
		}else if(orderCompareType == Order.SELL_ORDER_TYPE) {
			OrderDrivenComparator orderDrivenBuyComparator = new OrderDrivenComparator(Order.BUY_ORDER_TYPE);
			this.buyOrderDriven = new ConcurrentSkipListSet<>(orderDrivenBuyComparator);
			
			OrderDrivenComparator orderDrivenSellComparator = new OrderDrivenComparator(Order.SELL_ORDER_TYPE);
			this.sellOrderDriven = new ConcurrentSkipListSet<>(orderDrivenSellComparator);
			this.sellOrderDriven.addAll(orderC);
		}
	}
	
	
	
	private final class OrderDrivenComparator implements Comparator<Order> {

		private int orderCompareType;
		
		public OrderDrivenComparator(int orderCompareType) {
			this.orderCompareType = orderCompareType;
		}
		
		@Override
		public int compare(Order o1, Order o2) {
			int compareRes = 0;
			if(orderCompareType == Order.BUY_ORDER_TYPE) {
				compareRes = o2.getOrderPrice().compareTo(o1.getOrderPrice());
			}else if(orderCompareType == Order.SELL_ORDER_TYPE) {
				compareRes = o1.getOrderPrice().compareTo(o2.getOrderPrice());
			}
			
			compareRes = compareRes ==0?  o1.getOrderCreateTime().compareTo(o2.getOrderCreateTime()): compareRes;
			compareRes = compareRes ==0?  o1.getOrderId().compareTo(o2.getOrderId()): compareRes;
			return compareRes;
		}

	}
	
	
	public boolean addOrder(int orderType, Order order) {
		boolean res = false;
		if(orderType == Order.BUY_ORDER_TYPE) {
			res = this.buyOrderDriven.add(order);
		}else if(orderType == Order.SELL_ORDER_TYPE) {
			res = this.sellOrderDriven.add(order);
		}
		return res;
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

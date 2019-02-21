package com.wbo.currencyExchange.serviceImpl.matchServiceImpl;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wbo.currencyExchange.domain.MatchSequence;
import com.wbo.currencyExchange.domain.Order;
import com.wbo.currencyExchange.domain.OrderDriven;

@Service
public class MatchEngineServiceImpl {

	@Autowired
	MatchSequence matchSequence;
	
	private ConcurrentHashMap<Integer, OrderDriven> sequenceMap;
	private ExecutorService executor;
	private static volatile boolean matchWorkState = true;

	
	public void initMatchEngine() {
		
		sequenceMap = matchSequence.getSequenceMap();
		int currentSize = sequenceMap.size();
		
		executor = Executors.newFixedThreadPool(currentSize);
		
	}
	
	
	public static void stopMatch() {
		matchWorkState = false;
	}
	
	
	private class MatchForOrderDriven implements Runnable {
		
		private int orderDrivenId;
		
		public MatchForOrderDriven(int id) {
			this.orderDrivenId = id;
		}
		
		
		
		@Override
		public void run() {
			OrderDriven orderDriven= sequenceMap.get(orderDrivenId);
			ConcurrentSkipListSet<Order> buyOrderDriven = orderDriven.getBuyOrderDriven();
			ConcurrentSkipListSet<Order> sellOrderDriven = orderDriven.getSellOrderDriven();
			
			while(matchWorkState) {
				if(matchSuccess(buyOrderDriven, sellOrderDriven)) {
					// 再写个处理函数
					Order matchedBuyOrder = buyOrderDriven.pollFirst();
					Order matchedSellOrder = sellOrderDriven.pollFirst();
					
				}
			}
			
		}
		
		
		public boolean matchSuccess(ConcurrentSkipListSet<Order> buyOrderDriven, ConcurrentSkipListSet<Order> sellOrderDriven) {
			BigDecimal buyPrice = buyOrderDriven.first().getOrderPrice();
			BigDecimal sellPrice = sellOrderDriven.first().getOrderPrice();
			return buyPrice.compareTo(sellPrice) >=0;
		}
		
		
		
	}
	
}

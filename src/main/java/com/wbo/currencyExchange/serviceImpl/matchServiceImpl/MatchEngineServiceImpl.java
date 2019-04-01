package com.wbo.currencyExchange.serviceImpl.matchServiceImpl;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
	private ConcurrentHashMap<Integer, Future> futureMap;
	
	public void initMatchEngine() {
		
		sequenceMap = matchSequence.getSequenceMap();
		int currentSize = sequenceMap.size();
		
		final int THREAD_NUM = 4;
//		executor = Executors.newFixedThreadPool(currentSize);
		executor = new ThreadPoolExecutor(THREAD_NUM, THREAD_NUM, 60, TimeUnit.SECONDS, 
				new LinkedBlockingQueue<Runnable>());
		
		for(int id : sequenceMap.keySet()) {
			Future futureTmp = executor.submit(new MatchForOrderDriven(id));
			futureMap.put(id, futureTmp);
		}
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
					// 扔给mq，由清算系统处理
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

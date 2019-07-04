package com.wbo.currencyExchange.serviceImpl.matchServiceImpl;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;


import com.wbo.currencyExchange.domain.MatchSequence;
import com.wbo.currencyExchange.domain.Order;
import com.wbo.currencyExchange.domain.OrderDriven;
import com.wbo.currencyExchange.rabbitMQ.producer.DefaultMqSender;
import com.wbo.currencyExchange.rabbitMQ.producer.MatchedOrderMqSendEnvelop;
import com.wbo.currencyExchange.service.matchService.MatchEngineService;

@PropertySource("classpath:/sysconfig.properties")
@Service
public class MatchEngineServiceImpl implements MatchEngineService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	MatchSequence matchSequence;
	@Autowired
	DefaultMqSender mqSenderForMatchedOrder;
	
//	@Value("${matchSystem.ThreadNum}")
	private static final int N_CPU = Runtime.getRuntime().availableProcessors();
	
	@Value("${matchSystem.MatchTaskThreadQueue}")
	private static final int TASK_NUM = 20;
	
	private ConcurrentHashMap<Integer, OrderDriven> sequenceMap;
	private ThreadPoolExecutor executor;
	private static volatile boolean matchWorkState = false;
	private final ConcurrentHashMap<Integer, Future<Integer>> futureMap = new ConcurrentHashMap<>();
	private final CopyOnWriteArrayList<Integer> currencyIdList = new CopyOnWriteArrayList<>();
	
	
	
	/**
	 * 例子任务队列长度3，id有1，2，3.
	 * 
	 * 问题1：
	 * 		刚开始想着用ThreadPoolExecutor.DiscardPolicy，超出任务队列直接丢弃，但由于submit快于任务执行，导致任务队列堆积。
	 * 		比如(1,2,3)后再加的新任务1，2被丢弃，这时1执行完变成(2,3,3).这就会造成之后可能出现同一id有多个线程任务，造成线程问题。而且新增加的任务相当于随机，
	 * 		这样可能造成一些id的撮合出现饥饿。
	 * 解决1：
	 * 		给相同id的买卖盘加锁，锁的粒度比较小。
	 * 
	 * 问题2：
	 * 		想进一步解决，不要上面的锁。利用AbortPolicy淘汰。tryCatch线程池提交，当队列出现堆积时在catch中将生成nextID的currencyIdx--，
	 * 		或是添加一个boolean当throw exception 时下次就不重新计算id了。虽然之后新任务添加是顺序的。但旧任务完成是无序的，还是不行。
	 *		(1,2,3)先完成的可能是2 --> (1,3,1) -- > (1,1,2)这样还是会产生上述问题。还是必须有额外的同步方法。 
	 *
	 * 
	 */
	public void initMatchEngine() {
		
		sequenceMap = matchSequence.getSequenceMap();
		
//		executor = Executors.newFixedThreadPool(currentSize);
 		executor = new ThreadPoolExecutor(N_CPU+1, N_CPU+1, 60, TimeUnit.SECONDS, 
				new ArrayBlockingQueue<Runnable>(TASK_NUM), new ThreadPoolExecutor.DiscardPolicy());
		
		for(int id : sequenceMap.keySet()) {
			currencyIdList.add(id);
			Future<Integer> futureTmp = executor.submit(new MatchForOrderDriven(id));
			futureMap.put(id, futureTmp);
		}
		
		
		/*
		while(matchWorkState) {
//			logger.info("ActiveCount: "+executor.getActiveCount());
//			logger.info("TaskCount: "+executor.getTaskCount());
//			logger.info("CompletedTaskCount: "+executor.getCompletedTaskCount());
			int id = currencyIdList.get(nextCurrencyIdx());
			Future<Integer> futureTmp = executor.submit(new MatchForOrderDriven(id));
			futureMap.put(id, futureTmp);
			
			//解决cpu100%，sleep释放cpu资源
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			try {
//				Future<Integer> futureTmp = executor.submit(new MatchForOrderDriven(id));
//				futureMap.put(id, futureTmp);
//				outOfTaskQueue = false;
//			} catch (Exception e) {
//				logger.info("队列堆积: "+executor.getQueue().size());
//				outOfTaskQueue = true;
//			}
			
		}
		*/
		
	}
	
	
	private class MatchForOrderDriven implements Callable<Integer> {
		
		private int orderDrivenId;
		
		public MatchForOrderDriven(int id) {
			this.orderDrivenId = id;
		}
		
		@Override 
		public Integer call() {
			OrderDriven orderDriven= sequenceMap.get(orderDrivenId);
			ConcurrentSkipListSet<Order> buyOrderDriven = orderDriven.getBuyOrderDriven();
			ConcurrentSkipListSet<Order> sellOrderDriven = orderDriven.getSellOrderDriven();
			
			int matchTimes = 0;
			int unMatch = 0;
			long times = 0;
			
			while(true) {
				matchTimes++;
				times++;
//				if(times>2000000) {
//					logger.info("撮合ID"+orderDrivenId);
//					times=0;
//				}
				
				// 锁住相同currencyId的买卖盘。由于submit快于任务执行，导致任务队列堆积。之后可能出现同一id有多个线程任务，造成线程问题。
				// 如果不加锁，可能线程一刚matchSuccess，线程二pollFirst。线程一之后会得到两不匹配的Order(虽然不匹配之后也会校验)，或order为null。
				// 如果不加锁，matchSuccess中可能取到以被poll的旧值，导致错误的计算。
				synchronized (orderDriven) {
//					if(orderDrivenId==101 && times>1000000) {
//						logger.info("撮合ID"+orderDrivenId+"  "+sellOrderDriven.size());
//						logger.info("撮合ID"+sequenceMap);
//						times=0;
//					}
					if(matchSuccess(buyOrderDriven, sellOrderDriven)) {
						// 再写个处理函数
						Order matchedBuyOrder = buyOrderDriven.pollFirst();
						Order matchedSellOrder = sellOrderDriven.pollFirst();
						// 扔给mq，由清算系统处理
						sendMatchedOrder(Arrays.asList(matchedBuyOrder, matchedSellOrder));
						unMatch=0;
					}else {
						unMatch++;
					}
					
//					try {
//						Thread.sleep(1);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
					// 连续十次未匹配成功或匹配超过100次yield。
					if(unMatch>10 || matchTimes> 100) {
						unMatch = 0;
						matchTimes = 0;
						Thread.yield();
					}
				}
				
			}
			
		}
		
		private boolean matchSuccess(ConcurrentSkipListSet<Order> buyOrderDriven, ConcurrentSkipListSet<Order> sellOrderDriven) {
			if(buyOrderDriven == null || sellOrderDriven==null)
				throw new IllegalStateException("buy sell orderDriven is null");
			if(buyOrderDriven.size() ==0 || sellOrderDriven.size()==0)
				return false;
			
			int a = buyOrderDriven.size();
			int b = sellOrderDriven.size();
			BigDecimal buyPrice = buyOrderDriven.first().getOrderPrice();
			BigDecimal sellPrice = sellOrderDriven.first().getOrderPrice();
			return buyPrice.compareTo(sellPrice) >=0;
		}
		
	}

	
	public static void stopMatch() {
		matchWorkState = false;
	}
	
	
	int currencyIdx = 0;
	private int nextCurrencyIdx() {
		currencyIdx &= 0x7fffffff;
		return (currencyIdx++)%(currencyIdList.size());
	}
	
	
	@Override
	public void sendMatchedOrder(List<Order> orderGroup) {
		mqSenderForMatchedOrder.send(MatchedOrderMqSendEnvelop.MATCHED_ORDER_MQ, orderGroup);
	}
	
}

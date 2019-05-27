package com.wbo.currencyExchange.serviceImpl.matchServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wbo.currencyExchange.domain.DigitalCurrency;
import com.wbo.currencyExchange.domain.MatchSequence;
import com.wbo.currencyExchange.domain.Order;
import com.wbo.currencyExchange.domain.OrderDriven;
import com.wbo.currencyExchange.exception.GlobalException;
import com.wbo.currencyExchange.result.CodeMsg;
import com.wbo.currencyExchange.service.digitalCurrencyService.DigitalCurrencyService;
import com.wbo.currencyExchange.service.matchService.MatchEngineService;
import com.wbo.currencyExchange.service.matchService.SequenceService;
import com.wbo.currencyExchange.service.orderService.PlaceOrderService;

@Service
public class SequenceServiceImpl implements SequenceService{

	public static final AtomicBoolean isSequenceInit = new AtomicBoolean(false);
	
//	@Autowired
//	SequenceDao sequenceDao;
	@Autowired
	PlaceOrderService placeOrderService;
	@Autowired
	DigitalCurrencyService digitalCurrencyService;
	@Autowired
	MatchSequence matchSequence;
	@Autowired
	MatchEngineService matchEngineService;
	
	private ConcurrentHashMap<Integer, OrderDriven> sequenceMap;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Override
	public boolean placeOrderToSequence(Order order) {
		
		logger.info("orderorderorderorderorderorderorderorder:  "+order);
		if(order == null) {
			throw new GlobalException(CodeMsg.ORDER_NULL_ERROR);
		}
		
		Order addOrder;
		try {
			addOrder = (Order) order.clone();
		} catch (CloneNotSupportedException e) {
			logger.error("CloneNotSupportedException： "+order);
			return false;
		}
		
		sequenceMap = matchSequence.getSequenceMap();
		if(isSequenceInit.compareAndSet(false, true)) {
			initSequence();
		}
		
		if(addOrder.getOrderType() == Order.BUY_ORDER_TYPE) {
			placeBuyOrderToSequence(addOrder);
		}else if(addOrder.getOrderType() == Order.SELL_ORDER_TYPE) {
			placeSellOrderToSequence(addOrder);
		}
		return true;
	}
	
	// 买订单进队列
	private void placeBuyOrderToSequence(Order order) {
		int currencyId = order.getCurrencyId();
		OrderDriven curOrderDriven = sequenceMap.get(currencyId);
		if(curOrderDriven != null) {
			curOrderDriven.addOrder(Order.BUY_ORDER_TYPE, order);
		}else {
			throw new GlobalException(CodeMsg.ORDER_NULL_ERROR); 
		}
		
	}
	
	// 卖订单进队列
	private void placeSellOrderToSequence(Order order) {
		int currencyId = order.getCurrencyId();
		OrderDriven curOrderDriven = sequenceMap.get(currencyId);
		if(curOrderDriven != null) {
			curOrderDriven.addOrder(Order.SELL_ORDER_TYPE, order);
		}else {
			throw new GlobalException(CodeMsg.ORDER_NULL_ERROR); 
		}
	}
	
	// 初始化定序队列，买卖盘
	public synchronized void initSequence() {
		// 防止多次初始化
		if(!isSequenceInit.compareAndSet(false, true)) {
			return;
		}
		
		HashMap<Integer, HashMap<Integer, List<Order>>> notEndStateOrderMap = placeOrderService.getAllNotEndStateOrders();
		List<DigitalCurrency> digitalCurrencyList = digitalCurrencyService.getAllCurrency();
		
		HashMap<Integer, List<Order>> buyOrderNotEndStateMap = notEndStateOrderMap.get(Order.BUY_ORDER_TYPE);
		HashMap<Integer, List<Order>> sellOrderNotEndStateMap = notEndStateOrderMap.get(Order.SELL_ORDER_TYPE);
		
		sequenceMap = matchSequence.getSequenceMap();
		for(Entry<Integer, List<Order>> entry : buyOrderNotEndStateMap.entrySet()) {
			int curCurrencyId = entry.getKey();
			List<Order> buyOrderList = entry.getValue();
			List<Order> sellOrderList = sellOrderNotEndStateMap.get(curCurrencyId);
			OrderDriven orderDriven = new OrderDriven(buyOrderList, sellOrderList);
			sequenceMap.put(curCurrencyId, orderDriven);
		}
		
		for(DigitalCurrency currency : digitalCurrencyList) {
			int currencyId = currency.getCurrencyId();
			
			// 如果不存在，则表明订单中没有该币种，应在买卖盘中加入
			if(!sequenceMap.containsKey(currencyId)) {
				OrderDriven orderDriven = new OrderDriven();
				sequenceMap.put(currencyId, orderDriven);
			}
		}
		
		matchEngineService.initMatchEngine();
	}




	
	
}

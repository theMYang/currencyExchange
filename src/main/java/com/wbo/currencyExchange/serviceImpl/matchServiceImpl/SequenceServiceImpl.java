package com.wbo.currencyExchange.serviceImpl.matchServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wbo.currencyExchange.dao.matchDao.SequenceDao;
import com.wbo.currencyExchange.domain.Order;
import com.wbo.currencyExchange.domain.OrderDriven;
import com.wbo.currencyExchange.service.matchService.SequenceService;
import com.wbo.currencyExchange.service.orderService.PlaceOrderService;

@Service
public class SequenceServiceImpl implements SequenceService{

//	@Autowired
//	SequenceDao sequenceDao;
	@Autowired
	PlaceOrderService placeOrderService;
	
	ConcurrentHashMap<Integer, OrderDriven> sequenceMap = new ConcurrentHashMap<>();
	
	// 初始化定序队列，买卖盘
	public void initSequence() {
		HashMap<Integer, HashMap<Integer, List<Order>>> notEndStateOrderMap = placeOrderService.getAllNotEndStateOrders();
		
		HashMap<Integer, List<Order>> buyOrderNotEndStateMap = notEndStateOrderMap.get(OrderDriven.BUY_ORDER_TYPE);
		HashMap<Integer, List<Order>> sellOrderNotEndStateMap = notEndStateOrderMap.get(OrderDriven.SELL_ORDER_TYPE);
		
		for(Entry<Integer, List<Order>> entry : buyOrderNotEndStateMap.entrySet()) {
			int curCurrencyId = entry.getKey();
			List<Order> buyOrderList = entry.getValue();
			List<Order> sellOrderList = sellOrderNotEndStateMap.get(curCurrencyId);
			OrderDriven orderDriven = new OrderDriven(buyOrderList, sellOrderList);
			sequenceMap.put(curCurrencyId, orderDriven);
		}
	}
}

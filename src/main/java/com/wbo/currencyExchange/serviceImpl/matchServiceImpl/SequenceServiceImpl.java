package com.wbo.currencyExchange.serviceImpl.matchServiceImpl;

import java.util.HashMap;
import java.util.List;
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
	
	public void initSequence() {
		final int BUY_ORDER_TYPE = 1;
		final int SELL_ORDER_TYPE = 2;
		
		HashMap<Integer, List<List<Order>>> notEndStateOrderList = placeOrderService.getAllNotEndStateOrders();
		int orderListLength = notEndStateOrderList.size();
		for(int i=0; i<orderListLength; i++) {
			List<Order> curOrderList = notEndStateOrderList.get(i);
			int curCurrencyId = curOrderList.get(0).getCurrencyId();
			ConcurrentSkipListSet<Order> curOrderSet = new ConcurrentSkipListSet<>(curOrderList);
			// sequenceMap.put(curCurrencyId, curOrderSet);
		}
		return;
	}
}

package com.wbo.currencyExchange.serviceImpl.orderServiceImpl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wbo.currencyExchange.dao.orderDao.OrderDao;
import com.wbo.currencyExchange.domain.Order;
import com.wbo.currencyExchange.domain.UserLogin;
import com.wbo.currencyExchange.rabbitMQ.producer.DefaultMqSender;
import com.wbo.currencyExchange.rabbitMQ.producer.OrderMqSendEnvelop;
import com.wbo.currencyExchange.result.CodeMsg;
import com.wbo.currencyExchange.result.ResultCode;
import com.wbo.currencyExchange.service.matchService.SequenceService;
import com.wbo.currencyExchange.service.orderService.PlaceOrderService;
import com.wbo.currencyExchange.service.userService.UserAssetService;
import com.wbo.currencyExchange.service.userService.UserBalanceService;
import com.wbo.currencyExchange.util.SnowFlakeId;

@Service
public class PlaceOrderServiceImpl implements PlaceOrderService{

	@Autowired
	UserBalanceService userBalanceService;
	@Autowired
	OrderDao orderDao;
	@Autowired
	DefaultMqSender mqSenderForInsertOrder;
	@Autowired
	SequenceService sequenceService;
	@Autowired
	UserAssetService userAssetService;
	
	@Override
	public ResultCode placeBuyOrder(BigDecimal purchaseAmount, BigDecimal purchasePrice, int currencyId, UserLogin user) {
		int userId = user.getUserId();
		// 下订单时检查余额，并对余额进行冻结
		CodeMsg checkThenSetBalanCodeMsg = userBalanceService.checkThenSetBalance(purchaseAmount, purchasePrice, userId);
		if(checkThenSetBalanCodeMsg.getCode() < 0) {
			return ResultCode.error(checkThenSetBalanCodeMsg);
		}

		// 余额检查并冻结后，添加订单
		Order order = new Order();
		constructOrder(order, purchaseAmount, purchasePrice, currencyId, userId);
		insertOrderByMQ(order);
		
		// 订单进入定序模块
		orderToSequence(order);
		
		return ResultCode.msgData(checkThenSetBalanCodeMsg, userId);
	}

	
	@Override
	public ResultCode placeSellOrder(BigDecimal sellAmount, BigDecimal sellPrice, int currencyId, UserLogin user) {
		int userId = user.getUserId();
		// 下订单时检查资产，并对资产进行冻结
		CodeMsg checkThenSetAssetCodeMsg = userAssetService.checkThenSetAsset(sellAmount, currencyId, userId);
		
		
		return null;
	}
	
	
	
	private void orderToSequence(Order order) {
		sequenceService.placeOrderToSequence(order);
	}

	
	@Override
	public void insertOrderByMQ(Order order) {
		mqSenderForInsertOrder.send(OrderMqSendEnvelop.ORDER_FOR_INSERT_MQ, order);
	}

	@Override
	public boolean insertOrder(Order order) {
		boolean res = false;
		if(!insertOrderCheckField(order)) {
			return false;
		}
		
		int rows = orderDao.insertOrder(order);
		if(rows>0) {
			res = true;
		}
		return res;
	}
	
	
	
	private boolean insertOrderCheckField(Order order) {
		boolean res = true;
		final BigDecimal ZERO = new BigDecimal(0);
		
		res &= order.getOrderId() > 0;
		res &= order.getUserId() > 0;
		res &= order.getCurrencyId() > 0;
		res &= order.getOrderAmount().compareTo(ZERO) >0;
		res &= order.getOrderPrice().compareTo(ZERO) >0;
		res &= order.getOrderType() >0;
		res &= order.getOrderCreateTime() != null;
		
		return res;
	}

	private void constructOrder(Order order, BigDecimal purchaseAmount, BigDecimal purchasePrice, int currencyId, int userId) {
		SnowFlakeId idWorker = new SnowFlakeId(0, 0);
		long orderId = idWorker.nextId();
		final int ORDER_TYPE_BUY = 1;
		Timestamp orderCreateTime = new Timestamp(System.currentTimeMillis());
		
		order.setOrderId(orderId);
		order.setUserId(userId);
		order.setCurrencyId(currencyId);
		order.setOrderAmount(purchaseAmount);
		order.setOrderPrice(purchasePrice);
		order.setOrderType(ORDER_TYPE_BUY);
		order.setOrderCreateTime(orderCreateTime);
	}


	// 获取全部非终结态订单List, 并按照currencyId分List存放
	@Override
	public HashMap<Integer, HashMap<Integer, List<Order>>> getAllNotEndStateOrders() {
		List<Order> notEndStateOrderList = orderDao.getAllNotEndStateOrders();
		
		HashMap<Integer, List<Order>> buyOrderMap = new HashMap<>();
		HashMap<Integer, List<Order>> sellOrderMap = new HashMap<>();
		
		HashMap<Integer, HashMap<Integer, List<Order>>> map = new HashMap<>();
		
		int curCurrencyId = -1;
		for(int i=0; i<notEndStateOrderList.size(); i++) {
			Order tempOrder = notEndStateOrderList.get(i);
			int tempCurrencyId = tempOrder.getCurrencyId();
			int orderType = tempOrder.getOrderType();
			
			if(curCurrencyId != tempCurrencyId) {
				curCurrencyId = tempCurrencyId;
				buyOrderMap.put(tempCurrencyId, new ArrayList<>());
				sellOrderMap.put(tempCurrencyId, new ArrayList<>());
			}
			
			if(orderType == Order.BUY_ORDER_TYPE) {
				List<Order> tempBuyOrderList = buyOrderMap.get(tempCurrencyId);
				tempBuyOrderList.add(tempOrder);
			}else if(orderType == Order.SELL_ORDER_TYPE) {
				List<Order> tempSellOrderList = sellOrderMap.get(tempCurrencyId);
				tempSellOrderList.add(tempOrder);
			}
		}
		
		map.put(Order.BUY_ORDER_TYPE, buyOrderMap);
		map.put(Order.SELL_ORDER_TYPE, sellOrderMap);
		return map;
	}
	
	
	
	
	
}

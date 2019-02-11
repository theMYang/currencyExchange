package com.wbo.currencyExchange.serviceImpl.orderServiceImpl;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.reset;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wbo.currencyExchange.dao.orderDao.OrderDao;
import com.wbo.currencyExchange.domain.Order;
import com.wbo.currencyExchange.domain.OrderDriven;
import com.wbo.currencyExchange.domain.UserLogin;
import com.wbo.currencyExchange.rabbitMQ.producer.DefaultMqSender;
import com.wbo.currencyExchange.rabbitMQ.producer.OrderMqSendEnvelop;
import com.wbo.currencyExchange.result.CodeMsg;
import com.wbo.currencyExchange.result.ResultCode;
import com.wbo.currencyExchange.service.orderService.PlaceOrderService;
import com.wbo.currencyExchange.service.userService.UserBalanceService;
import com.wbo.currencyExchange.util.SnowFlakeId;
import com.wbo.currencyExchange.util.UUIDUtil;

@Service
public class PlaceOrderServiceImpl implements PlaceOrderService{

	@Autowired
	UserBalanceService userBalanceService;
	@Autowired
	OrderDao orderDao;
	@Autowired
	DefaultMqSender mqSenderForInsertOrder;
	@Autowired
	Order order;
	
	@Override
	public ResultCode placeBuyOrder(BigDecimal purchaseAmount, BigDecimal purchasePrice, int currencyId, UserLogin user) {
		int userId = user.getUserId();
		// 下订单时检查余额，并对余额进行冻结
		CodeMsg checkThenSetBalanCodeMsg = userBalanceService.checkThenSetBalance(purchaseAmount, purchasePrice, userId);
		if(checkThenSetBalanCodeMsg.getCode() < 0) {
			return ResultCode.error(checkThenSetBalanCodeMsg);
		}

		// 余额检查并冻结后，添加订单
		constructOrder(purchaseAmount, purchasePrice, currencyId, userId);
		insertOrderByMQ(order);
		
		// 订单进入定序模块
		
		
		return ResultCode.msgData(checkThenSetBalanCodeMsg, userId);
	}

	
	private void orderToSequence() {
		
	}
	
	
	
	@Override
	public ResultCode placeSellOrder(BigDecimal purchaseAmount, BigDecimal purchasePrice, int currencyId, UserLogin user) {
		return null;
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
		
		return res;
	}

	private void constructOrder(BigDecimal purchaseAmount, BigDecimal purchasePrice, int currencyId, int userId) {
		SnowFlakeId idWorker = new SnowFlakeId(0, 0);
		long orderId = idWorker.nextId();
		final int ORDER_TYPE_BUY = 1;
		
		order.setOrderId(orderId);
		order.setUserId(userId);
		order.setCurrencyId(currencyId);
		order.setOrderAmount(purchaseAmount);
		order.setOrderPrice(purchasePrice);
		order.setOrderType(ORDER_TYPE_BUY);
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
			
			if(orderType == OrderDriven.BUY_ORDER_TYPE) {
				List<Order> tempBuyOrderList = buyOrderMap.get(tempCurrencyId);
				tempBuyOrderList.add(tempOrder);
			}else if(orderType == OrderDriven.SELL_ORDER_TYPE) {
				List<Order> tempSellOrderList = sellOrderMap.get(tempCurrencyId);
				tempSellOrderList.add(tempOrder);
			}
		}
		
		map.put(OrderDriven.BUY_ORDER_TYPE, buyOrderMap);
		map.put(OrderDriven.SELL_ORDER_TYPE, sellOrderMap);
		return map;
	}
	
	
	
	
	
	
	
}

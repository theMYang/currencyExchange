package com.wbo.currencyExchange.serviceImpl.orderServiceImpl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wbo.currencyExchange.dao.orderDao.OrderDao;
import com.wbo.currencyExchange.domain.Order;
import com.wbo.currencyExchange.domain.UserLogin;
import com.wbo.currencyExchange.exception.GlobalException;
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

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
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
		constructOrder(order, purchaseAmount, purchasePrice, currencyId, userId, Order.BUY_ORDER_TYPE);
//		insertOrderByMQ(order);
		boolean insertSucc = insertOrder(order);
		if(!insertSucc) {
			return ResultCode.error(CodeMsg.ORDER_INSERT_ERROR);
		}
			
		
		// 订单进入定序模块
		orderToSequence(order);
		
		return ResultCode.msgData(checkThenSetBalanCodeMsg, userId);
	}

	
	@Override
	public ResultCode placeSellOrder(BigDecimal sellAmount, BigDecimal sellPrice, int currencyId, UserLogin user) {
		int userId = user.getUserId();
		// 下订单时检查货币资产，并对资产进行冻结
		CodeMsg checkThenSetAssetCodeMsg = userAssetService.checkThenSetAsset(sellAmount, currencyId, userId);
		if(checkThenSetAssetCodeMsg.getCode() < 0) {
			return ResultCode.error(checkThenSetAssetCodeMsg);
		}
		
		// 余额检查并冻结后，添加订单
		constructOrder(order, sellAmount, sellPrice, currencyId, userId, Order.SELL_ORDER_TYPE);
		boolean insertSucc = insertOrder(order);
		if(!insertSucc) {
			return ResultCode.error(CodeMsg.ORDER_INSERT_ERROR);
		}
//		insertOrderByMQ(order);
		
		// 订单进入定序模块
		orderToSequence(order);
		
		return ResultCode.msgData(checkThenSetAssetCodeMsg, userId);
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

	private void constructOrder(Order order, BigDecimal orderAmount, BigDecimal orderPrice, int currencyId, int userId, int orderType) {
		SnowFlakeId idWorker = new SnowFlakeId(0, 0);
		long orderId = idWorker.nextId();
		Timestamp orderCreateTime = new Timestamp(System.currentTimeMillis());
		
		order.setOrderId(orderId);
		order.setUserId(userId);
		order.setCurrencyId(currencyId);
		order.setOrderAmount(orderAmount);
		order.setOrderPrice(orderPrice);
		order.setOrderType(orderType);
		order.setOrderCreateTime(orderCreateTime);
		order.setDealAmount(new BigDecimal(0));
		order.setDealPrice(new BigDecimal(0));
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
			
			// 直接buyOrderMap.comtainsKey()判断就好啊？？（这样写虽然不会错）
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


	
	
	
	
	
	@Override
	public int updateOrdersForClearing(List<Order> clearingOrderList) {
		int updateOrdersForClearingNum = orderDao.updateOrdersForClearing(clearingOrderList);
		return updateOrdersForClearingNum;
	}


	@Override
	public int updateOrderForClearing(Order order) {
		int updateOrderForClearingNum = orderDao.updateOrderForClearing(order);
		return updateOrderForClearingNum;
	}
	
	
//	@Override
//	public void acceptOrder(BigDecimal sellAmount, BigDecimal sellPrice, int currencyId, UserLogin user, int OrderType) {
//		int userId = user.getUserId();
//		constructOrder(order, sellAmount, sellPrice, currencyId, userId, OrderType);
//		insertOrderByMQ(order);
//	}
}

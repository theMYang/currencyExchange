package com.wbo.currencyExchange.service.orderService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.wbo.currencyExchange.domain.Order;
import com.wbo.currencyExchange.domain.UserLogin;
import com.wbo.currencyExchange.result.ResultCode;

@Service
public interface PlaceOrderService {

	// 接收订单
//	public void acceptOrder(BigDecimal sellAmount, BigDecimal sellPrice, int currencyId, UserLogin user, int OrderType);
	
	// 下单买货币
	public ResultCode placeBuyOrder(BigDecimal purchaseAmount, BigDecimal purchasePrice, int currencyId,  UserLogin user);
	
	// 下单卖货币
	public ResultCode placeSellOrder(BigDecimal sellAmount, BigDecimal sellPrice, int currencyId,  UserLogin user);
	
	// 订单表插入订单
	public boolean insertOrder(Order order);
	
	// 订单表插入订单，通过消息队列
	public void insertOrderByMQ(Order order);
	
	// 获取所有非终结态订单
	// HashMap<ORDER_TYPE, HashMap<currencyId, List<Order>>>
	public HashMap<Integer, HashMap<Integer, List<Order>>> getAllNotEndStateOrders();
	
	// 清算系统批量更新order
	public int updateOrdersForClearing(List<Order> clearingOrderLists);
	
	// 清算系统更新order
	public int updateOrderForClearing(Order order);
}

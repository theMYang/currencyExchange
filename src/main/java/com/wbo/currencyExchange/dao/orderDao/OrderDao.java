package com.wbo.currencyExchange.dao.orderDao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.wbo.currencyExchange.domain.Order;

@Mapper
public interface OrderDao {

	public int insertOrder(Order order);
	
	public List<Order> getAllNotEndStateOrders();
	
	public int updateOrdersForClearing(@Param("list") List<Order> clearingOrderLists);
	
	public int updateOrderForClearing(Order order);
}

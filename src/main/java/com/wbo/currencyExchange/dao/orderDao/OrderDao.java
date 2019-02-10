package com.wbo.currencyExchange.dao.orderDao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.wbo.currencyExchange.domain.Order;

@Mapper
public interface OrderDao {

	public int insertOrder(Order order);
	
	public List<Order> getAllNotEndStateOrders();
}

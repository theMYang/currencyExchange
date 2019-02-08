package com.wbo.currencyExchange.dao.orderDao;

import org.apache.ibatis.annotations.Mapper;

import com.wbo.currencyExchange.domain.Order;

@Mapper
public interface OrderDao {

	public int insertOrder(Order order);
}

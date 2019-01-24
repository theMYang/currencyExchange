package com.wbo.currencyExchange.dao.userDao;

import java.math.BigDecimal;

import javax.websocket.server.PathParam;

import org.apache.ibatis.annotations.Mapper;

import com.wbo.currencyExchange.domain.UserBalance;

@Mapper
public interface UserBalanceDao {

	public int insertUserBalance(UserBalance userBalance);
	
	public UserBalance getBalanceByUserId(@PathParam("userId") int userId);
}

package com.wbo.currencyExchange.dao.UserDao;

import org.apache.ibatis.annotations.Mapper;

import com.wbo.currencyExchange.domain.UserBalance;

@Mapper
public interface UserBalanceDao {

	public int insertUserBalance(UserBalance userBalance);
}

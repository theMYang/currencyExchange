package com.wbo.currencyExchange.dao.userDao;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Mapper;

import com.wbo.currencyExchange.domain.UserBalance;
import com.wbo.currencyExchange.domain.UserLogin;

@Mapper
public interface UserBalanceDao {

	public int insertUserBalance(UserBalance userBalance);
	
	public boolean isBalanceEnough(BigDecimal requiredBalance, int userId);
}

package com.wbo.currencyExchange.dao.userDao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.wbo.currencyExchange.domain.UserBalance;

@Mapper
public interface UserBalanceDao {

	public int insertUserBalance(UserBalance userBalance);
	
	public UserBalance getBalanceByUserId(@Param("userId") int userId);
	
	public int freezeBalanceForOrderDB(UserBalance userBalance);
}

package com.wbo.currencyExchange.service.userService;

import java.math.BigDecimal;

import com.wbo.currencyExchange.domain.UserBalance;

public interface UserBalanceService {

	public int insertUserBalance(UserBalance userBalance);
	
	public BigDecimal surplusBalance(BigDecimal requiredBalance, int userId);
	
	public UserBalance getBalanceByUserId(int userId);
	
	public boolean freezeBalanceForOrder(BigDecimal requiredBalance, int userId);
	
	public boolean checkThenSetBalance();
	
}

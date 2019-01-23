package com.wbo.currencyExchange.service.userService;

import java.math.BigDecimal;

import com.wbo.currencyExchange.domain.UserBalance;
import com.wbo.currencyExchange.domain.UserLogin;

public interface UserBalanceService {

	public int insertUserBalance(UserBalance userBalance);
	
	public boolean isBalanceEnough(BigDecimal requiredBalance, int userId);
}

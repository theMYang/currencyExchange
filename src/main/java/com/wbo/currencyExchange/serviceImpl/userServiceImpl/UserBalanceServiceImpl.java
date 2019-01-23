package com.wbo.currencyExchange.serviceImpl.userServiceImpl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wbo.currencyExchange.dao.userDao.UserBalanceDao;
import com.wbo.currencyExchange.domain.UserBalance;
import com.wbo.currencyExchange.domain.UserLogin;
import com.wbo.currencyExchange.service.userService.UserBalanceService;

@Service
public class UserBalanceServiceImpl implements UserBalanceService{

	@Autowired
	UserBalanceDao userBalanceDao;

	@Override
	public int insertUserBalance(UserBalance userBalance) {
		int insertUserBalanceResult = userBalanceDao.insertUserBalance(userBalance);
		return insertUserBalanceResult;
	}

	@Override
	public boolean isBalanceEnough(BigDecimal requiredBalance, int userId) {
		boolean isBalanceEnough = userBalanceDao.isBalanceEnough(requiredBalance, userId);
		return isBalanceEnough;
	}
	
	
}

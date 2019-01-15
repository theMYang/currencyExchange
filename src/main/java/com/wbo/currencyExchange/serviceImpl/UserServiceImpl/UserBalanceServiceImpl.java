package com.wbo.currencyExchange.serviceImpl.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wbo.currencyExchange.dao.UserDao.UserBalanceDao;
import com.wbo.currencyExchange.domain.UserBalance;
import com.wbo.currencyExchange.service.UserService.UserBalanceService;

@Service
public class UserBalanceServiceImpl implements UserBalanceService{

	@Autowired
	UserBalanceDao userBalanceDao;

	@Override
	public int insertUserBalance(UserBalance userBalance) {
		int insertUserBalanceResult = userBalanceDao.insertUserBalance(userBalance);
		return insertUserBalanceResult;
	}
	
	
}

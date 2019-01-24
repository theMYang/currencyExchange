package com.wbo.currencyExchange.serviceImpl.userServiceImpl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wbo.currencyExchange.dao.userDao.UserBalanceDao;
import com.wbo.currencyExchange.domain.UserBalance;
import com.wbo.currencyExchange.domain.UserLogin;
import com.wbo.currencyExchange.redis.BalanceKey;
import com.wbo.currencyExchange.redis.RedisService;
import com.wbo.currencyExchange.service.userService.UserBalanceService;

@Service
public class UserBalanceServiceImpl implements UserBalanceService{

	@Autowired
	UserBalanceDao userBalanceDao;
	@Autowired
	RedisService<Integer, BigDecimal> reidsService;

	@Override
	public int insertUserBalance(UserBalance userBalance) {
		int insertUserBalanceResult = userBalanceDao.insertUserBalance(userBalance);
		//没修改redis
		return insertUserBalanceResult;
	}
	
	
	
	@Override
	public BigDecimal surplusBalance(BigDecimal requiredBalance, int userId) {
		BigDecimal possessBalance = reidsService.getString(BalanceKey.BALANCE, userId, BigDecimal.class);
		if(possessBalance==null) {
			possessBalance = setRedisOfBalance(userId);
		}
//		boolean isBalanceEnough = requiredBalance.compareTo(possessBalance) <=0;
		BigDecimal surplusBalance = possessBalance.subtract(requiredBalance);
		return surplusBalance;
	}
	
	private BigDecimal setRedisOfBalance(int userId) {
		UserBalance balance = getBalanceByUserId(userId);
		reidsService.setIfAbsentString(BalanceKey.BALANCE, userId, balance.getBalanceAmount());
		reidsService.setIfAbsentString(BalanceKey.FREEZE_BALANCE, userId, balance.getFreezeAmount());
		BigDecimal possessbalance = balance.getBalanceAmount();
		return possessbalance;
	}
	
	public UserBalance getBalanceByUserId(int userId) {
		UserBalance balance = userBalanceDao.getBalanceByUserId(userId);
		return balance;
	}

	@Override
	public boolean freezeBalanceForOrder(BigDecimal requiredBalance, int userId) {
		boolean res = false;
		res = reidsService.subByBigDecimal(BalanceKey.BALANCE, userId, requiredBalance);
		if(!res) {
			return res;
		}
		res = reidsService.incrByBigDecimal(BalanceKey.FREEZE_BALANCE, userId, requiredBalance);
		if(!res) {
			return res;
		}
		return true;
	}
	
	
	
	
}

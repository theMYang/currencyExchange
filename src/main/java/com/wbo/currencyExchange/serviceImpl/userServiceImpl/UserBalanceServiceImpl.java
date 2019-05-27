package com.wbo.currencyExchange.serviceImpl.userServiceImpl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wbo.currencyExchange.dao.userDao.UserBalanceDao;
import com.wbo.currencyExchange.domain.UserBalance;
import com.wbo.currencyExchange.exception.GlobalException;
import com.wbo.currencyExchange.rabbitMQ.producer.BalanceMqSendEnvelop;
import com.wbo.currencyExchange.rabbitMQ.producer.DefaultMqSender;
import com.wbo.currencyExchange.redis.BalanceKey;
import com.wbo.currencyExchange.redis.RedisService;
import com.wbo.currencyExchange.result.CodeMsg;
import com.wbo.currencyExchange.service.userService.UserBalanceService;

/**
 * @author hasee
 *
 */
@Service
public class UserBalanceServiceImpl implements UserBalanceService{

	@Autowired
	UserBalanceDao userBalanceDao;
	@Autowired
	RedisService<Integer, BigDecimal> redisService;
	@Autowired
	DefaultMqSender mqSender;

	@Override
	//测试
	public int insertUserBalance(UserBalance userBalance) {
		int insertUserBalanceResult = userBalanceDao.insertUserBalance(userBalance);
		//没修改redis
		return insertUserBalanceResult;
	}
	
	
	
	@Override
	public CodeMsg checkThenSetBalance(BigDecimal purchaseAmount, BigDecimal purchasePrice, int userId) {
		BigDecimal requiredBalance = purchaseAmount.multiply(purchasePrice);
		CodeMsg resCodeMsg = null;
		
		synchronized (this) {
			BigDecimal surplusBalance = this.surplusBalance(requiredBalance, userId);
			boolean isBalanceEnough = surplusBalance.compareTo(BigDecimal.ZERO) >=0;
			if(isBalanceEnough) {
				boolean freezeBalance = this.freezeBalanceForOrderReids(requiredBalance, userId);
				if(freezeBalance) {
					resCodeMsg = CodeMsg.BALANCE_SET_SUCCESS;
				}else {
					resCodeMsg = CodeMsg.BALANCE_SHORT_ERROR;
				}
			}else {
				resCodeMsg = CodeMsg.BALANCE_SHORT_ERROR;
			}
		}
		
		if(resCodeMsg.getCode() >0) {
			// sendFreezeBalanceMq(requiredBalance, userId);
			UserBalance userBalance = new UserBalance(); 
			userBalance.setFreezeAmount(requiredBalance);
			userBalance.setUserId(userId);
			boolean res = freezeBalanceForOrderDB(userBalance);
		}
		return resCodeMsg;
	}
	
	
	// 放入消息队列，对数据库的余额进行冻结
	public void sendFreezeBalanceMq(BigDecimal requiredBalance, int userId) {
		UserBalance userBalance = new UserBalance(); 
		userBalance.setFreezeAmount(requiredBalance);
		userBalance.setUserId(userId);
		mqSender.send(BalanceMqSendEnvelop.BALANCE_FOR_ORDER_MQ, userBalance);
	}
	
	
	
	
	@Override
	public BigDecimal surplusBalance(BigDecimal requiredBalance, int userId) {
		BigDecimal possessBalance = redisService.getString(BalanceKey.BALANCE, userId, BigDecimal.class);
		BigDecimal freezeBalance = redisService.getString(BalanceKey.FREEZE_BALANCE, userId, BigDecimal.class);
		if(possessBalance==null || freezeBalance==null) {
			setRedisOfBalance(userId);
			possessBalance = redisService.getString(BalanceKey.BALANCE, userId, BigDecimal.class);
			freezeBalance = redisService.getString(BalanceKey.FREEZE_BALANCE, userId, BigDecimal.class);
		}
		
		// 剩余余额=余额-冻结余额-待冻结余额
		BigDecimal surplusBalance = possessBalance.subtract(freezeBalance).subtract(requiredBalance);
		return surplusBalance;
	}
	
	
	
	/**
	* @Description: 在redis中设置用户余额信息，包括余额和冻结余额
	* @param userId
	* @return boolean    
	*/
	private void setRedisOfBalance(int userId) {
		UserBalance balance = getBalanceByUserId(userId);
		
		if(balance == null) {
			throw new GlobalException(CodeMsg.BALANCE_NULL_ERROR);
		}
		redisService.setIfAbsentString(BalanceKey.BALANCE, userId, balance.getBalanceAmount());
		redisService.setIfAbsentString(BalanceKey.FREEZE_BALANCE, userId, balance.getFreezeAmount());
	}
	
	
	
	@Override
	public UserBalance getBalanceByUserId(int userId) {
		UserBalance balance = userBalanceDao.getBalanceByUserId(userId);
		return balance;
	}
	
	@Override
	public boolean freezeBalanceForOrderReids(BigDecimal freezeBalance, int userId) {
		boolean res = false;
//		res = redisService.subByBigDecimal(BalanceKey.BALANCE, userId, freezeBalance);
//		if(!res) {
//			return res;
//		}
		res = redisService.incrByBigDecimal(BalanceKey.FREEZE_BALANCE, userId, freezeBalance);
		if(!res) {
			setRedisOfBalance(userId);
			res = redisService.incrByBigDecimal(BalanceKey.FREEZE_BALANCE, userId, freezeBalance);
		}
		return res;
	}
	
	@Override
	public boolean freezeBalanceForOrderDB(UserBalance userBalance) {
		int affectRows = userBalanceDao.freezeBalanceForOrderDB(userBalance);
		if(affectRows == 1) {
			return true;
		}else {
			return false;
		}
	}



	@Override
	public boolean updateBalanceForClearing(UserBalance userBalance) {
		int affectRows = userBalanceDao.updateBalanceForClearing(userBalance);
		if(affectRows == 1) {
			return true;
		}else {
			return false;
		}
	}
	
}

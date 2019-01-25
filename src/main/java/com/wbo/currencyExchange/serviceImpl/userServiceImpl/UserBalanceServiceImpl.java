package com.wbo.currencyExchange.serviceImpl.userServiceImpl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wbo.currencyExchange.dao.userDao.UserBalanceDao;
import com.wbo.currencyExchange.domain.UserBalance;
import com.wbo.currencyExchange.rabbitMQ.producer.BalanceMqSendEnvelop;
import com.wbo.currencyExchange.rabbitMQ.producer.DefaultMqSender;
import com.wbo.currencyExchange.redis.BalanceKey;
import com.wbo.currencyExchange.redis.RedisService;
import com.wbo.currencyExchange.result.CodeMsg;
import com.wbo.currencyExchange.result.ResultCode;
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
	RedisService<Integer, BigDecimal> reidsService;
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
		final BigDecimal ZERO = new BigDecimal(0);
		
		synchronized (this) {
			BigDecimal surplusBalance = this.surplusBalance(requiredBalance, userId);
			boolean isBalanceEnough = surplusBalance.compareTo(ZERO) >=0;
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
			sendFreezeBalanceMq(requiredBalance, userId);
		}
		return resCodeMsg;
	}
	
	
	public void sendFreezeBalanceMq(BigDecimal requiredBalance, int userId) {
		UserBalance userBalance = new UserBalance(); 
		userBalance.setFreezeAmount(requiredBalance);
		userBalance.setUserId(userId);
		mqSender.send(BalanceMqSendEnvelop.BALANCE_FOR_ORDER_MQ, userBalance);
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
	
	
	
	/**
	* @Description: 在redis中设置用户余额信息，包括余额和冻结余额
	* @param userId
	* @return BigDecimal    
	*/
	private BigDecimal setRedisOfBalance(int userId) {
		UserBalance balance = getBalanceByUserId(userId);
		reidsService.setIfAbsentString(BalanceKey.BALANCE, userId, balance.getBalanceAmount());
		reidsService.setIfAbsentString(BalanceKey.FREEZE_BALANCE, userId, balance.getFreezeAmount());
		BigDecimal possessbalance = balance.getBalanceAmount();
		return possessbalance;
	}
	
	
	
	@Override
	public UserBalance getBalanceByUserId(int userId) {
		UserBalance balance = userBalanceDao.getBalanceByUserId(userId);
		return balance;
	}
	
	@Override
	public boolean freezeBalanceForOrderReids(BigDecimal requiredBalance, int userId) {
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
	
	@Override
	public boolean freezeBalanceForOrderDB(UserBalance userBalance) {
		userBalanceDao.freezeBalanceForOrderDB(userBalance);
		return true;
	}
	
}

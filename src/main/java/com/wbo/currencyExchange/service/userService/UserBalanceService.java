package com.wbo.currencyExchange.service.userService;

import java.math.BigDecimal;

import com.wbo.currencyExchange.domain.UserBalance;
import com.wbo.currencyExchange.result.CodeMsg;

public interface UserBalanceService {

	public int insertUserBalance(UserBalance userBalance);
	
	// 扣除待花费后的余额量
	public BigDecimal surplusBalance(BigDecimal requiredBalance, int userId);
	
	// 获取Balance对象
	public UserBalance getBalanceByUserId(int userId);
	
	// redis中下订单后冻结余额  成功返回true 失败返回false
	 public boolean freezeBalanceForOrderReids(BigDecimal requiredBalance, int userId);
	// 数据库中下订单后冻结余额  成功返回true 失败返回false
	public boolean freezeBalanceForOrderDB(UserBalance userBalance);
	
	// 下订单时检查余额，并对余额进行冻结（对redis中没有缓存的用户余额进行设置）
	public CodeMsg checkThenSetBalance(BigDecimal purchaseAmount, BigDecimal purchasePrice, int userId);
	
	// 清算系统更新
	public boolean updateBalanceForClearing(UserBalance userBalance);
	
}

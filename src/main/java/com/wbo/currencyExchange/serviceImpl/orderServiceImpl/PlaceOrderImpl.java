package com.wbo.currencyExchange.serviceImpl.orderServiceImpl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wbo.currencyExchange.domain.UserLogin;
import com.wbo.currencyExchange.result.CodeMsg;
import com.wbo.currencyExchange.result.ResultCode;
import com.wbo.currencyExchange.service.OrderService.PlaceOrderService;
import com.wbo.currencyExchange.service.userService.UserBalanceService;

@Service
public class PlaceOrderImpl implements PlaceOrderService{

	private static final BigDecimal ZERO = new BigDecimal(0);
	
	@Autowired
	UserBalanceService userBalanceService;
	
	@Override
	public ResultCode placeBuyOrder(BigDecimal purchaseAmount, BigDecimal purchasePrice, UserLogin user) {
		int userId = user.getUserId();
		checkThenSetBalance(purchaseAmount, purchasePrice, userId);
		BigDecimal requiredBalance = purchaseAmount.multiply(purchasePrice);
		ResultCode resultCode = null;
		
		synchronized (this) {
			
			BigDecimal surplusBalance = userBalanceService.surplusBalance(requiredBalance, userId);
			boolean isBalanceEnough = surplusBalance.compareTo(ZERO) >=0;
			if(isBalanceEnough) {
				boolean freezeBalance = userBalanceService.freezeBalanceForOrder(requiredBalance, userId);
				if(freezeBalance) {
					resultCode = ResultCode.error(CodeMsg.BALANCE_SHORT_ERROR, surplusBalance);
				}
					
			}else {
				resultCode = ResultCode.error(CodeMsg.BALANCE_SHORT_ERROR, surplusBalance);
			}
		}
		return resultCode;
	}

	@Override
	public ResultCode placeSellOrder(BigDecimal purchaseAmount, BigDecimal purchasePrice, UserLogin user) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
	
	
	
	
}

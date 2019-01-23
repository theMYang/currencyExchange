package com.wbo.currencyExchange.serviceImpl.orderServiceImpl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wbo.currencyExchange.domain.UserLogin;
import com.wbo.currencyExchange.result.ResultCode;
import com.wbo.currencyExchange.service.OrderService.PlaceOrder;
import com.wbo.currencyExchange.service.userService.UserBalanceService;

@Service
public class PlaceOrderImpl implements PlaceOrder{

	@Autowired
	UserBalanceService userBalanceService;
	
	@Override
	public ResultCode placeBuyOrder(BigDecimal purchaseAmount, BigDecimal purchasePrice, UserLogin user) {
		BigDecimal requiredBalance = purchaseAmount.multiply(purchasePrice);
		int userId = user.getUserId();
		boolean isBalanceEnough = userBalanceService.isBalanceEnough(requiredBalance, userId);
		return null;
	}

	@Override
	public ResultCode placeSellOrder(BigDecimal purchaseAmount, BigDecimal purchasePrice, UserLogin user) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
	
	
	
	
}

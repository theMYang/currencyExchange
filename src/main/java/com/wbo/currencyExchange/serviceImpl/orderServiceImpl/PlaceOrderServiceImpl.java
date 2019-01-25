package com.wbo.currencyExchange.serviceImpl.orderServiceImpl;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wbo.currencyExchange.domain.UserLogin;
import com.wbo.currencyExchange.result.CodeMsg;
import com.wbo.currencyExchange.result.ResultCode;
import com.wbo.currencyExchange.service.OrderService.PlaceOrderService;
import com.wbo.currencyExchange.service.userService.UserBalanceService;

@Service
public class PlaceOrderServiceImpl implements PlaceOrderService{

	@Autowired
	UserBalanceService userBalanceService;
	
	@Override
	public ResultCode placeBuyOrder(BigDecimal purchaseAmount, BigDecimal purchasePrice, UserLogin user) {
		int userId = user.getUserId();
		CodeMsg checkThenSetBalanCodeMsg = userBalanceService.checkThenSetBalance(purchaseAmount, purchasePrice, userId);
		if(checkThenSetBalanCodeMsg.getCode() < 0) {
			return ResultCode.error(checkThenSetBalanCodeMsg);
		}
		return null;
	}

	@Override
	public ResultCode placeSellOrder(BigDecimal purchaseAmount, BigDecimal purchasePrice, UserLogin user) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
	
	
	
	
}

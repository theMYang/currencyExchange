package com.wbo.currencyExchange.service.OrderService;

import java.math.BigDecimal;

import com.wbo.currencyExchange.domain.UserLogin;
import com.wbo.currencyExchange.result.ResultCode;

public interface PlaceOrderService {

	// 下单买货币
	public ResultCode placeBuyOrder(BigDecimal purchaseAmount, BigDecimal purchasePrice,  UserLogin user);
	
	// 下单卖货币
	public ResultCode placeSellOrder(BigDecimal purchaseAmount, BigDecimal purchasePrice,  UserLogin user);
	
}

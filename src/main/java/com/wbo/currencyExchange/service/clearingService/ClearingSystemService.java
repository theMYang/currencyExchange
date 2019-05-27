package com.wbo.currencyExchange.service.clearingService;

import java.math.BigDecimal;

import org.springframework.context.annotation.Scope;

import com.wbo.currencyExchange.domain.Order;

public interface ClearingSystemService {

	public boolean clearingOrder(Order matchedBuyOrder, Order matchedSellOrder);
	
	public void clearingBalanceNAsset(Order matchedBuyOrder, Order matchedSellOrder, BigDecimal dealAmount, BigDecimal dealPrice);
}

package com.wbo.currencyExchange.service.userService;

import java.math.BigDecimal;

import com.wbo.currencyExchange.domain.UserAsset;
import com.wbo.currencyExchange.domain.UserLogin;
import com.wbo.currencyExchange.result.CodeMsg;

public interface UserAssetService {

	// 下订单时检查资产，并对相应资产进行冻结（对redis中没有缓存的用户资产进行设置）
	public CodeMsg checkThenSetAsset(BigDecimal sellAmount, int currencyId, int userId);
	
	
	public BigDecimal surplusAssetCurrency(BigDecimal sellAmount, int currencyId, int userId);
	
	
	public UserAsset getUserAssetByUserId(int userId, int currencyId);
	
	
	public boolean freezeCurrencyForAssetReids(BigDecimal sellAmount, int userId, int currencyId);
	
	
	public boolean freezeAssertForOrderDB(UserAsset userAsset);
}

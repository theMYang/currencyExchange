package com.wbo.currencyExchange.serviceImpl.userServiceImpl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wbo.currencyExchange.dao.userDao.UserAssetDao;
import com.wbo.currencyExchange.domain.UserAsset;
import com.wbo.currencyExchange.domain.UserLogin;
import com.wbo.currencyExchange.exception.GlobalException;
import com.wbo.currencyExchange.redis.AssetKey;
import com.wbo.currencyExchange.redis.BalanceKey;
import com.wbo.currencyExchange.redis.RedisService;
import com.wbo.currencyExchange.result.CodeMsg;
import com.wbo.currencyExchange.service.userService.UserAssetService;

@Service
public class UserAssetServiceImpl implements UserAssetService {

	@Autowired
	RedisService<String, BigDecimal> redisService;
	@Autowired
	UserAssetDao userAssetDao;
	
	@Override
	public CodeMsg checkThenSetAsset(BigDecimal sellAmount,  int currencyId, int userId) {
		CodeMsg resCodeMsg = null;
		final BigDecimal ZERO = new BigDecimal(0);
		
		synchronized (this) {
			BigDecimal surplusAssetCurrency = this.surplusAssetCurrency(sellAmount, currencyId, userId);
		}
		
		return null;
	}

	@Override
	public BigDecimal surplusAssetCurrency(BigDecimal sellAmount, int currencyId, int userId) {
		String prefix = userId + "," + currencyId;
		BigDecimal assetCurrencyAmount = redisService.getString(AssetKey.ASSET, prefix, BigDecimal.class);
		BigDecimal assetCurrencyFreezeAmount = redisService.getString(AssetKey.FREEZE_ASSET, prefix, BigDecimal.class);
		
		if(assetCurrencyAmount==null || assetCurrencyFreezeAmount==null) {
			boolean setRedis = setRedisOfAsset(userId, currencyId);
			if(!setRedis) {
				throw new GlobalException(CodeMsg.REDIS_SET_ERROR);
			}
			assetCurrencyAmount = redisService.getString(AssetKey.ASSET, prefix, BigDecimal.class);
			assetCurrencyFreezeAmount = redisService.getString(AssetKey.FREEZE_ASSET, prefix, BigDecimal.class);
		}
		
		BigDecimal surplusAssetCurrency = assetCurrencyAmount.subtract(assetCurrencyAmount).subtract(sellAmount);
		return surplusAssetCurrency;
		// 待办...
	}

	
	private boolean setRedisOfAsset(int userId, int currencyId) {
		boolean setRedisRes = false;
		String prefix = userId + "," + currencyId;
		UserAsset userAsset = getUserAssetByUserId(userId, currencyId);
		BigDecimal currencyAmount = new BigDecimal(0);
		BigDecimal freezeAmount = new BigDecimal(0);
		
		// null时该用户无相关货币
		if(userAsset != null) {
			currencyAmount = userAsset.getCurrencyAmount();
			freezeAmount = userAsset.getFreezeAmount();
		}
		setRedisRes = redisService.setIfAbsentString(AssetKey.ASSET, prefix, currencyAmount);
		setRedisRes = setRedisRes && redisService.setIfAbsentString(AssetKey.FREEZE_ASSET, prefix, freezeAmount);
		return setRedisRes;
	}
	
	
	public UserAsset getUserAssetByUserId(int userId, int currencyId) {
		UserAsset userAsset = userAssetDao.getUserAssetByUserId(userId, currencyId);
		return userAsset;
	}
}

package com.wbo.currencyExchange.serviceImpl.userServiceImpl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wbo.currencyExchange.dao.userDao.UserAssetDao;
import com.wbo.currencyExchange.domain.UserAsset;
import com.wbo.currencyExchange.exception.GlobalException;
import com.wbo.currencyExchange.rabbitMQ.producer.AssetMqSendEnvelop;
import com.wbo.currencyExchange.rabbitMQ.producer.DefaultMqSender;
import com.wbo.currencyExchange.redis.AssetKey;
import com.wbo.currencyExchange.redis.RedisService;
import com.wbo.currencyExchange.result.CodeMsg;
import com.wbo.currencyExchange.service.userService.UserAssetService;

@Service
public class UserAssetServiceImpl implements UserAssetService {

	@Autowired
	RedisService<String, BigDecimal> redisService;
	@Autowired
	UserAssetDao userAssetDao;
	@Autowired
	DefaultMqSender mqSender;
	@Autowired
	UserAsset userAsset;
	
	@Override
	public CodeMsg checkThenSetAsset(BigDecimal sellAmount,  int currencyId, int userId) {
		CodeMsg resCodeMsg = null;
		final BigDecimal ZERO = new BigDecimal(0);
		
		synchronized (this) {
			BigDecimal surplusAssetCurrencyAmount = this.surplusAssetCurrency(sellAmount, currencyId, userId);
			boolean isAssetCurrencyEnough = surplusAssetCurrencyAmount.compareTo(ZERO) >=0;
			if(isAssetCurrencyEnough) {
				boolean freezeAssetCurrency = this.freezeCurrencyForAssetReids(sellAmount, userId, currencyId);
				if(freezeAssetCurrency) {
					resCodeMsg = CodeMsg.ASSET_SET_SUCCESS;
				}else {
					resCodeMsg = CodeMsg.ASSET_SHORT_ERROR;
				}
			}else {
				resCodeMsg = CodeMsg.ASSET_SHORT_ERROR;
			}
		}
		
		if(resCodeMsg.getCode() >0) {
			userAsset.setCurrencyId(currencyId);
			userAsset.setUserId(userId);
			userAsset.setFreezeAmount(sellAmount);
			boolean res = freezeAssertForOrderDB(userAsset);
//			sendFreezeAssetCurrencyMq(sellAmount, userId, currencyId);
		}
		return resCodeMsg;
	}

	
	// 放入消息队列，对数据库的余额进行冻结
	public void sendFreezeAssetCurrencyMq(BigDecimal sellAmount, int userId, int currencyId) {
		UserAsset userAsset = new UserAsset();
		userAsset.setCurrencyId(currencyId);
		userAsset.setUserId(userId);
		userAsset.setFreezeAmount(sellAmount);
		mqSender.send(AssetMqSendEnvelop.ASSET_FREEZE_CURRENCY_MQ, userAsset);
	}
	
	
	@Override
	public BigDecimal surplusAssetCurrency(BigDecimal sellAmount, int currencyId, int userId) {
		String prefix = userId + "," + currencyId;
		BigDecimal assetCurrencyAmount = redisService.getString(AssetKey.ASSET, prefix, BigDecimal.class);
		BigDecimal assetCurrencyFreezeAmount = redisService.getString(AssetKey.FREEZE_ASSET, prefix, BigDecimal.class);
		
		if(assetCurrencyAmount==null || assetCurrencyFreezeAmount==null) {
			setRedisOfAsset(userId, currencyId);
			assetCurrencyAmount = redisService.getString(AssetKey.ASSET, prefix, BigDecimal.class);
			assetCurrencyFreezeAmount = redisService.getString(AssetKey.FREEZE_ASSET, prefix, BigDecimal.class);
		}
		
		BigDecimal surplusAssetCurrency = assetCurrencyAmount.subtract(assetCurrencyFreezeAmount).subtract(sellAmount);
		return surplusAssetCurrency;
	}

	
	private void setRedisOfAsset(int userId, int currencyId) {
		String prefix = userId + "," + currencyId;
		UserAsset userAsset = getUserAssetByUserId(userId, currencyId);
		BigDecimal currencyAmount = new BigDecimal(0);
		BigDecimal freezeAmount = new BigDecimal(0);
		
		// null时该用户无相关货币
		if(userAsset != null) {
			currencyAmount = userAsset.getCurrencyAmount();
			freezeAmount = userAsset.getFreezeAmount();
		}
		redisService.setIfAbsentString(AssetKey.ASSET, prefix, currencyAmount);
		redisService.setIfAbsentString(AssetKey.FREEZE_ASSET, prefix, freezeAmount);
	}
	
	
	@Override
	public boolean freezeCurrencyForAssetReids(BigDecimal sellAmount, int userId, int currencyId) {
		boolean res = false;
		String prefix = userId + "," + currencyId;
		res = redisService.incrByBigDecimal(AssetKey.FREEZE_ASSET, prefix, sellAmount);
		if(!res) {
			setRedisOfAsset(userId, currencyId);
			res = redisService.incrByBigDecimal(AssetKey.FREEZE_ASSET, prefix, sellAmount);
		}
		return res;
	}
	

	@Override
	public boolean freezeAssertForOrderDB(UserAsset userAsset) {
		int affectRows = userAssetDao.freezeAssertForOrderDB(userAsset);
		if(affectRows == 1) {
			return true;
		}else {
			return false;
		}
	}
	
	
	public UserAsset getUserAssetByUserId(int userId, int currencyId) {
		UserAsset userAsset = userAssetDao.getUserAssetByUserId(userId, currencyId);
		return userAsset;
	}


	@Override
	public boolean updateAssertForClearing(UserAsset userAsset) {
		int affectRows = userAssetDao.updateAssertForClearing(userAsset);
		if(affectRows >0) {
			return true;
		}else {
			return false;
		}
	}
}

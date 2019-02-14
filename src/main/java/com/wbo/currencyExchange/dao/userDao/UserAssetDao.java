package com.wbo.currencyExchange.dao.userDao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.wbo.currencyExchange.domain.UserAsset;

@Mapper
public interface UserAssetDao {

	public UserAsset getUserAssetByUserId(@Param("userId") int userId, @Param("currencyId") int currencyId);
	
	public int freezeAssertForOrderDB(UserAsset userAsset);
}

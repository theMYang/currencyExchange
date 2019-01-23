package com.wbo.currencyExchange.dao.userDao;

import org.apache.ibatis.annotations.Mapper;

import com.wbo.currencyExchange.domain.UserInfo;

@Mapper
public interface UserInfoDao {

	public int insertUserInfo(UserInfo userInfo);
}

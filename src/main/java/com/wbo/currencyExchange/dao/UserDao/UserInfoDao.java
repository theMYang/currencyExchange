package com.wbo.currencyExchange.dao.UserDao;

import org.apache.ibatis.annotations.Mapper;

import com.wbo.currencyExchange.domain.UserInfo;

@Mapper
public interface UserInfoDao {

	public int insertUserInfo(UserInfo userInfo);
}

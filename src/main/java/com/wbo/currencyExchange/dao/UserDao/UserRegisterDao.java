package com.wbo.currencyExchange.dao.UserDao;

import org.apache.ibatis.annotations.Mapper;

import com.wbo.currencyExchange.domain.UserLogin;

@Mapper
public interface UserRegisterDao {

	public int insertUserLogin(UserLogin user);
}

package com.wbo.currencyExchange.dao.userDao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.wbo.currencyExchange.domain.UserLogin;

@Mapper
public interface UserRegisterDao {

	public int insertUserLogin(UserLogin user);
}

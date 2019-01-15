package com.wbo.currencyExchange.dao.UserDao;

import org.apache.ibatis.annotations.Mapper;

import com.wbo.currencyExchange.domain.UserAccount;

@Mapper
public interface UserAccountDao {

	public int insertUserAccount(UserAccount userAccount);
}

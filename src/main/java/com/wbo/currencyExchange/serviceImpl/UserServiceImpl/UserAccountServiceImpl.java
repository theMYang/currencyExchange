package com.wbo.currencyExchange.serviceImpl.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wbo.currencyExchange.dao.UserDao.UserAccountDao;
import com.wbo.currencyExchange.domain.UserAccount;
import com.wbo.currencyExchange.service.UserService.UserAccountService;

@Service
public class UserAccountServiceImpl implements UserAccountService{

	@Autowired
	UserAccountDao userAccountDao;
	
	@Override
	public int insertUserAccount(UserAccount userAccount) {
		int insertUserAccountResult = userAccountDao.insertUserAccount(userAccount);
		return insertUserAccountResult;
	}

	
}

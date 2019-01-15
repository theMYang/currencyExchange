package com.wbo.currencyExchange.serviceImpl.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wbo.currencyExchange.dao.UserDao.UserLoginDao;
import com.wbo.currencyExchange.domain.UserLogin;


@Service
public class UserLoginServiceImpl{
	
	@Autowired
	UserLoginDao userLoginDao;
	
	public UserLogin getById(int id) {
		return userLoginDao.getById(id);
	}
}

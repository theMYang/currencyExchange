package com.wbo.currencyExchange.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wbo.currencyExchange.dao.UserLoginDao;
import com.wbo.currencyExchange.domain.UserLogin;


@Service
public class UserLoginServiceImpl {
	
	@Autowired
	UserLoginDao userLoginDao;
	
	public UserLogin getById(int id) {
		return userLoginDao.getById(id);
	}
}

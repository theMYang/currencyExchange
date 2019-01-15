package com.wbo.currencyExchange.serviceImpl.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wbo.currencyExchange.dao.UserDao.UserInfoDao;
import com.wbo.currencyExchange.domain.UserInfo;
import com.wbo.currencyExchange.service.UserService.UserInfoService;

@Service
public class UserInfoServiceImpl implements UserInfoService{

	@Autowired
	UserInfoDao userInfoDao;
	
	public int insertUserInfo(UserInfo userInfo) {
		int insertUserInfoResult = userInfoDao.insertUserInfo(userInfo);
		return insertUserInfoResult;
	}
}

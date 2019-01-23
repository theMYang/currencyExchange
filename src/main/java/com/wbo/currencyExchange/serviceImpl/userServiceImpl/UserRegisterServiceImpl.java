package com.wbo.currencyExchange.serviceImpl.userServiceImpl;

import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wbo.currencyExchange.dao.userDao.UserRegisterDao;
import com.wbo.currencyExchange.domain.UserAccount;
import com.wbo.currencyExchange.domain.UserBalance;
import com.wbo.currencyExchange.domain.UserInfo;
import com.wbo.currencyExchange.domain.UserLogin;
import com.wbo.currencyExchange.exception.GlobalException;
import com.wbo.currencyExchange.result.CodeMsg;
import com.wbo.currencyExchange.service.userService.UserAccountService;
import com.wbo.currencyExchange.service.userService.UserBalanceService;
import com.wbo.currencyExchange.service.userService.UserInfoService;
import com.wbo.currencyExchange.service.userService.UserRegisterService;
import com.wbo.currencyExchange.util.MD5Util;

@Service
public class UserRegisterServiceImpl implements UserRegisterService{

	@Autowired
	UserRegisterDao userRegisterDao;
	@Autowired
	UserInfoService userInfoService;
	@Autowired
	UserAccountService userAccountService;
	@Autowired
	UserBalanceService userBalanceService;
	
	@Transactional
	public int insertUserLogin(UserLogin userLogin) {
		//添加用户登录表
		MD5Util.EncoderByMd5ForUserLogin(userLogin);
		userRegisterDao.insertUserLogin(userLogin);
		int userId = userLogin.getUserId() ;
		if(userId<=0) 
			throw new GlobalException(CodeMsg.SERVER_ERROR);
		
		
		//添加用户信息表
		UserInfo userInfo = new UserInfo();
		userInfo.setUserId(userId);
		userInfo.setLoginName(userLogin.getLoginName());
		userInfo.setMobilePhone(userLogin.getMobilePhone());
		userInfoService.insertUserInfo(userInfo);
		if(userInfo.getUserId()<=0) 
			throw new GlobalException(CodeMsg.SERVER_ERROR);
		
		//添加用户账户表
		UserAccount userAccount = new UserAccount();
		userAccount.setUserId(userId);
		userAccountService.insertUserAccount(userAccount);
		if(userAccount.getAccountId()<=0)
			throw new GlobalException(CodeMsg.SERVER_ERROR);
		
		//添加用户余额表
		UserBalance userBalance = new UserBalance();
		userBalance.setUserId(userId);
		userBalance.setAccountId(userAccount.getAccountId());
		userBalance.setBalanceType(0);
		userBalanceService.insertUserBalance(userBalance);
		if(userBalance.getBalanceId()<=0)
			throw new GlobalException(CodeMsg.SERVER_ERROR);
		
		return userId;
	}
}

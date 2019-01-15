package com.wbo.currencyExchange.serviceImpl.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wbo.currencyExchange.dao.UserDao.UserRegisterDao;
import com.wbo.currencyExchange.domain.UserAccount;
import com.wbo.currencyExchange.domain.UserBalance;
import com.wbo.currencyExchange.domain.UserInfo;
import com.wbo.currencyExchange.domain.UserLogin;
import com.wbo.currencyExchange.service.UserService.UserAccountService;
import com.wbo.currencyExchange.service.UserService.UserBalanceService;
import com.wbo.currencyExchange.service.UserService.UserInfoService;
import com.wbo.currencyExchange.service.UserService.UserRegisterService;

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
	
	public int insertUserLogin(UserLogin userLogin) {
		//添加用户登录表
		int insertUserLoginResult = userRegisterDao.insertUserLogin(userLogin);
		int userLoginId = userLogin.getUserLoginId() ;
		// if(userLoginId== null || userLoginId<=0)
		
		
		//添加用户信息表
		UserInfo userInfo = new UserInfo();
		userInfo.setUserLoginId(userLoginId);
		userInfo.setLoginName(userLogin.getLoginName());
		userInfo.setMobilePhone(userLogin.getMobilePhone());
		int insertUserInfoResult = userInfoService.insertUserInfo(userInfo);
		
		//添加用户账户表
		UserAccount userAccount = new UserAccount();
		userAccount.setUserId(userInfo.getUserId());
		int insertUserAccountResult = userAccountService.insertUserAccount(userAccount);
		
		//添加用户余额表
		UserBalance userBalance = new UserBalance();
		userBalance.setUserId(userInfo.getUserId());
		userBalance.setAccountId(userAccount.getAccountId());
		userBalance.setBalanceType(0);
		userBalanceService.insertUserBalance(userBalance);
		return userBalance.getBalanceId();
	}
}

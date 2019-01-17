package com.wbo.currencyExchange.serviceImpl.UserServiceImpl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wbo.currencyExchange.dao.UserDao.UserLoginDao;
import com.wbo.currencyExchange.domain.UserLogin;
import com.wbo.currencyExchange.redis.RedisService;
import com.wbo.currencyExchange.redis.UserKey;
import com.wbo.currencyExchange.result.CodeMsg;
import com.wbo.currencyExchange.service.UserService.UserLoginService;
import com.wbo.currencyExchange.util.MD5Util;
import com.wbo.currencyExchange.util.UUIDUtil;


@Service
public class UserLoginServiceImpl implements UserLoginService{
	
	@Autowired
	UserLoginDao userLoginDao;
	@Autowired
	RedisService<String, UserLogin> redisService;
	
	public CodeMsg getUserLogin(HttpServletResponse response, UserLogin userLogin) {
		MD5Util.EncoderByMd5ForUserLogin(userLogin);
		UserLogin userLoginRet = userLoginDao.getUserLogin(userLogin);
		if(userLoginRet == null) {
			return CodeMsg.LOGIN_ERROR;
		}
		
		addCookie(response, userLoginRet);
		return CodeMsg.SUCCESS;
	}
	
	public UserLogin getByToken(HttpServletResponse response, String token) {
		if(token.isEmpty()) {
			return null;
		}
		UserLogin userLogin = redisService.getString(UserKey.token, token, UserLogin.class);
		//延长有效期
		if(userLogin != null) {
			addCookie(response, userLogin);
		}
		return userLogin;
	}
	
	
	private void addCookie(HttpServletResponse response, UserLogin userLogin) {
		String token = UUIDUtil.uuid();
		redisService.setString(UserKey.token, token, userLogin);
		Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
		cookie.setMaxAge(UserKey.token.getExpireSeconds());
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
}

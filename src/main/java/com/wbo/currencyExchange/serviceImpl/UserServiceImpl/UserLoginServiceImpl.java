package com.wbo.currencyExchange.serviceImpl.UserServiceImpl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.wbo.currencyExchange.dao.UserDao.UserLoginDao;
import com.wbo.currencyExchange.domain.UserLogin;
import com.wbo.currencyExchange.redis.RedisService;
import com.wbo.currencyExchange.redis.UserKey;
import com.wbo.currencyExchange.result.CodeMsg;
import com.wbo.currencyExchange.result.ResultCode;
import com.wbo.currencyExchange.service.UserService.UserLoginService;
import com.wbo.currencyExchange.util.MD5Util;
import com.wbo.currencyExchange.util.UUIDUtil;


@Service
public class UserLoginServiceImpl implements UserLoginService{
	
	public static final String COOKIE_NAME_TOKEN = "token";
	
	@Autowired
	UserLoginDao userLoginDao;
	@Autowired
	RedisService<String, UserLogin> redisService;
	
	public CodeMsg getUserLoginById(HttpServletResponse response, UserLogin userLogin) {
		MD5Util.EncoderByMd5ForUserLogin(userLogin);
		UserLogin userLoginRet = userLoginDao.getUserLoginById(userLogin);
		if(userLoginRet == null) {
			return CodeMsg.LOGIN_ERROR;
		}
		
		String token = UUIDUtil.uuid();
		redisService.setString(UserKey.token, token, userLoginRet);
		Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
		cookie.setMaxAge(UserKey.token.getExpireSeconds());
		cookie.setPath("/");
		response.addCookie(cookie);
		return CodeMsg.SUCCESS;
	}
}

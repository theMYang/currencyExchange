package com.wbo.currencyExchange.service.userService;

import org.springframework.stereotype.Service;

import com.wbo.currencyExchange.domain.UserLogin;

public interface UserRegisterService {

	public int insertUserLogin(UserLogin user);
}

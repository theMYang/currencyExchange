package com.wbo.currencyExchange.redis;

public class UserKey extends BasePrefix{

	public static final int TOKEN_EXPIRE = 3600*24*3;
	
	private  UserKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	
	public static final UserKey token = new UserKey(TOKEN_EXPIRE, "token");
}

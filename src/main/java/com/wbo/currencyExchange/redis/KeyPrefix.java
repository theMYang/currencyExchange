package com.wbo.currencyExchange.redis;

public interface KeyPrefix {

	public int getExpireSeconds();
	
	public String getPrefix();
}

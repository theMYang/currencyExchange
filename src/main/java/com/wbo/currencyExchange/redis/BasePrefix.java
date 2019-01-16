package com.wbo.currencyExchange.redis;

public abstract class BasePrefix implements KeyPrefix{

	private int expireSeconds;
	private String prefix;
	private static final int NOT_EXPIRE = -1;
	
	//-1代表不过期
	public BasePrefix(String prefix) {
		this(NOT_EXPIRE, prefix);
	} 
	
	public BasePrefix(int expireSeconds, String prefix) {
		this.expireSeconds = expireSeconds;
		this.prefix = prefix;
	} 
	
	public int getExpireSeconds() {
		return expireSeconds;
	}
	public String getPrefix() {
		String className = this.getClass().getSimpleName();
		return className + ":" +prefix;
	}
	
	
}

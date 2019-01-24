package com.wbo.currencyExchange.redis;

public class BalanceKey extends BasePrefix {

	public static final int BALANCE_EXPIRE = 3600*24*3;
	
	private  BalanceKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	
	public static final BalanceKey BALANCE = new BalanceKey(BALANCE_EXPIRE, "balance");
	public static final BalanceKey FREEZE_BALANCE = new BalanceKey(BALANCE_EXPIRE, "freezeBalance");
	
}

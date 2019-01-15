package com.wbo.currencyExchange.domain;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

@Component
public class UserAccount {

	private int accountId;
	private int userId;
	private int accountState;
	private int accountType;
	private int accountLevel;
	private Timestamp applyAccountTime;
	
	
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getAccountState() {
		return accountState;
	}
	public void setAccountState(int accountState) {
		this.accountState = accountState;
	}
	public int getAccountType() {
		return accountType;
	}
	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}
	public int getAccountLevel() {
		return accountLevel;
	}
	public void setAccountLevel(int accountLevel) {
		this.accountLevel = accountLevel;
	}
	public Timestamp getApplyAccountTime() {
		return applyAccountTime;
	}
	public void setApplyAccountTime(Timestamp applyAccountTime) {
		this.applyAccountTime = applyAccountTime;
	}
	
	
}

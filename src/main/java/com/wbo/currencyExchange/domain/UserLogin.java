package com.wbo.currencyExchange.domain;

import java.io.Serializable;

public class UserLogin implements Serializable{
	
	private int userId;
	private String loginName;
	private transient String password;
	private int userState;
	private String mobilePhone;
	
	public UserLogin() {
		
	}
	
	public UserLogin(String loginName, String password, String mobilePhone) {
		this.loginName = loginName;
		this.password = password;
		this.mobilePhone = mobilePhone;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getUserState() {
		return userState;
	}
	public void setUserState(int userState) {
		this.userState = userState;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	
	@Override
	public String toString() {
		return "UserLogin [userId=" + userId + ", loginName=" + loginName + ", userState=" + userState
				+ ", mobilePhone=" + mobilePhone + "]";
	}
}

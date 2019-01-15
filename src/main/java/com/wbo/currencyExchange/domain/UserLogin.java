package com.wbo.currencyExchange.domain;

import java.io.Serializable;

public class UserLogin implements Serializable{
	
	private int userLoginId;
	private String loginName;
	private transient String password;
	private int userState;
	private String mobilePhone;
	
	public int getUserLoginId() {
		return userLoginId;
	}
	public void setUserLoginId(int userLoginId) {
		this.userLoginId = userLoginId;
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
		return "UserLogin [userLoginId=" + userLoginId + ", loginName=" + loginName + ", password=" + password
				+ ", userState=" + userState + ", mobilePhone=" + mobilePhone + "]";
	}
	
}

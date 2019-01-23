package com.wbo.currencyExchange.domain;

import java.sql.Date;
import java.sql.Timestamp;

import org.springframework.stereotype.Component;

@Component
public class UserInfo {

	private int userInfId;
	private int userId;
	private String loginName;
	private String userName;
	private String profileUrl;
	private int identityCardType;
	private String identityCardNo;
	private String mobilePhone;
	private String userEmail;
	private String gender;
	private int creditValue;
	private Date userBirthday;
	private Timestamp registerTime;
	
	
	public int getUserInfId() {
		return userInfId;
	}
	public void setUserInfId(int userInfId) {
		this.userInfId = userInfId;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getProfileUrl() {
		return profileUrl;
	}
	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}
	public int getIdentityCardType() {
		return identityCardType;
	}
	public void setIdentityCardType(int identityCardType) {
		this.identityCardType = identityCardType;
	}
	public String getIdentityCardNo() {
		return identityCardNo;
	}
	public void setIdentityCardNo(String identityCardNo) {
		this.identityCardNo = identityCardNo;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getCreditValue() {
		return creditValue;
	}
	public void setCreditValue(int creditValue) {
		this.creditValue = creditValue;
	}
	public Date getUserBirthday() {
		return userBirthday;
	}
	public void setUserBirthday(Date userBirthday) {
		this.userBirthday = userBirthday;
	}
	public Timestamp getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(Timestamp registerTime) {
		this.registerTime = registerTime;
	}
	
	
}

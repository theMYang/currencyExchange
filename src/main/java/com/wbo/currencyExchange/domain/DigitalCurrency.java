package com.wbo.currencyExchange.domain;

import java.math.BigDecimal;

public class DigitalCurrency {
	
	private int currencyId;
	private String currencyCode;
	private String currencyEnName;
	private String currencyChName;
	private String issueTime;
	private int issueAmount;
	private int circulationAmount;
	private BigDecimal issuePrice;
	private String officialWebsite;
	private String currencyIntro;
	private int currencyOrder;
	private int isShow;
	
	
	public int getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(int currencyId) {
		this.currencyId = currencyId;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getCurrencyEnName() {
		return currencyEnName;
	}
	public void setCurrencyEnName(String currencyEnName) {
		this.currencyEnName = currencyEnName;
	}
	public String getCurrencyChName() {
		return currencyChName;
	}
	public void setCurrencyChName(String currencyChName) {
		this.currencyChName = currencyChName;
	}
	public String getIssueTime() {
		return issueTime;
	}
	public void setIssueTime(String issueTime) {
		this.issueTime = issueTime;
	}
	public int getIssueAmount() {
		return issueAmount;
	}
	public void setIssueAmount(int issueAmount) {
		this.issueAmount = issueAmount;
	}
	public int getCirculationAmount() {
		return circulationAmount;
	}
	public void setCirculationAmount(int circulationAmount) {
		this.circulationAmount = circulationAmount;
	}
	public BigDecimal getIssuePrice() {
		return issuePrice;
	}
	public void setIssuePrice(BigDecimal issuePrice) {
		this.issuePrice = issuePrice;
	}
	public String getOfficialWebsite() {
		return officialWebsite;
	}
	public void setOfficialWebsite(String officialWebsite) {
		this.officialWebsite = officialWebsite;
	}
	public String getCurrencyIntro() {
		return currencyIntro;
	}
	public void setCurrencyIntro(String currencyIntro) {
		this.currencyIntro = currencyIntro;
	}
	public int getCurrencyOrder() {
		return currencyOrder;
	}
	public void setCurrencyOrder(int currencyOrder) {
		this.currencyOrder = currencyOrder;
	}
	public int getIsShow() {
		return isShow;
	}
	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}
	
	
}

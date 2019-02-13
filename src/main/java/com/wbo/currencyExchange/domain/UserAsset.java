package com.wbo.currencyExchange.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class UserAsset implements Serializable{

	private int assetId;
	private int userId;
	private int currencyId;
	private BigDecimal currencyAmount;
	private BigDecimal freezeAmount;
	
	
	public int getAssetId() {
		return assetId;
	}
	public void setAssetId(int assetId) {
		this.assetId = assetId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(int currencyId) {
		this.currencyId = currencyId;
	}
	public BigDecimal getCurrencyAmount() {
		return currencyAmount;
	}
	public void setCurrencyAmount(BigDecimal currencyAmount) {
		this.currencyAmount = currencyAmount;
	}
	public BigDecimal getFreezeAmount() {
		return freezeAmount;
	}
	public void setFreezeAmount(BigDecimal freezeAmount) {
		this.freezeAmount = freezeAmount;
	}
	
	
	@Override
	public String toString() {
		return "UserAsset {assetId=" + assetId + ", userId=" + userId + ", currencyId=" + currencyId
				+ ", currencyAmount=" + currencyAmount + ", freezeAmount=" + freezeAmount + "}";
	}
	
	
	
}

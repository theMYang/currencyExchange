package com.wbo.currencyExchange.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class UserBalance implements Serializable {
	
	private int balanceId;
	private int accountId;
	private int userId;
	private int contractId;
	private Timestamp changeTime;
	
	private BigDecimal freezeAmount = BigDecimal.ZERO;
	private BigDecimal balanceAmount = BigDecimal.ZERO;
	private int balanceType;
	
	
	public int getBalanceId() {
		return balanceId;
	}
	public void setBalanceId(int balanceId) {
		this.balanceId = balanceId;
	}
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
	public int getContractId() {
		return contractId;
	}
	public void setContractId(int contractId) {
		this.contractId = contractId;
	}
	public Timestamp getChangeTime() {
		return changeTime;
	}
	public void setChangeTime(Timestamp changeTime) {
		this.changeTime = changeTime;
	}
	public BigDecimal getFreezeAmount() {
		return freezeAmount;
	}
	public void setFreezeAmount(BigDecimal freezeAmount) {
		this.freezeAmount = freezeAmount;
	}
	public BigDecimal getBalanceAmount() {
		return balanceAmount;
	}
	public void setBalanceAmount(BigDecimal balanceAmount) {
		this.balanceAmount = balanceAmount;
	}
	public int getBalanceType() {
		return balanceType;
	}
	public void setBalanceType(int balanceType) {
		this.balanceType = balanceType;
	}
	
	
	@Override
	public String toString() {
		return "UserBalance [balanceId=" + balanceId + ", accountId=" + accountId + ", userId=" + userId
				+ ", contractId=" + contractId + ", changeTime=" + changeTime + ", freezeAmount=" + freezeAmount
				+ ", balanceAmount=" + balanceAmount + ", balanceType=" + balanceType + "]";
	}
}

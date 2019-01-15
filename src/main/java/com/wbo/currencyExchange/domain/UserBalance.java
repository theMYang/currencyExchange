package com.wbo.currencyExchange.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.springframework.stereotype.Component;

@Component
public class UserBalance {
	
	private int balanceId;
	private int accountId;
	private int userId;
	private int balanceSource;
	private int contractId;
	private Timestamp changeTime;
	private BigDecimal balanceChange;
	private BigDecimal balanceAmount;
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
	public int getBalanceSource() {
		return balanceSource;
	}
	public void setBalanceSource(int balanceSource) {
		this.balanceSource = balanceSource;
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
	public BigDecimal getBalanceChange() {
		return balanceChange;
	}
	public void setBalanceChange(BigDecimal balanceChange) {
		this.balanceChange = balanceChange;
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


}

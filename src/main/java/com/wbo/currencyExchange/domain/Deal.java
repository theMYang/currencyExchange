package com.wbo.currencyExchange.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Deal {

	private int dealId;
	private Long buyOrderId;
	private Long sellOrderId;
	private BigDecimal dealAmount;
	private BigDecimal dealPrice;
	private Timestamp dealTime;
	private BigDecimal dealCharge;
	
	
	public int getDealId() {
		return dealId;
	}
	public void setDealId(int dealId) {
		this.dealId = dealId;
	}
	public Long getBuyOrderId() {
		return buyOrderId;
	}
	public void setBuyOrderId(Long buyOrderId) {
		this.buyOrderId = buyOrderId;
	}
	public Long getSellOrderId() {
		return sellOrderId;
	}
	public void setSellOrderId(Long sellOrderId) {
		this.sellOrderId = sellOrderId;
	}
	public BigDecimal getDealAmount() {
		return dealAmount;
	}
	public void setDealAmount(BigDecimal dealAmount) {
		this.dealAmount = dealAmount;
	}
	public BigDecimal getDealPrice() {
		return dealPrice;
	}
	public void setDealPrice(BigDecimal dealPrice) {
		this.dealPrice = dealPrice;
	}
	public Timestamp getDealTime() {
		return dealTime;
	}
	public void setDealTime(Timestamp dealTime) {
		this.dealTime = dealTime;
	}
	public BigDecimal getDealCharge() {
		return dealCharge;
	}
	public void setDealCharge(BigDecimal dealCharge) {
		this.dealCharge = dealCharge;
	}
	
	
	
	
}

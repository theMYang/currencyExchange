package com.wbo.currencyExchange.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import org.springframework.stereotype.Component;

@Component
public class Order implements Serializable{

	private int orderId;
	private int userId;
	private int currencyId;
	private BigDecimal orderAmount;
	private BigDecimal orderPrice;
	private int orderState;
	private int orderType;
	private BigDecimal dealAmount;
	private BigDecimal dealPrice;
	private BigDecimal dealCharge;
	private Timestamp orderCreateTime;
	private Timestamp orderCancelTime;
	private Timestamp endStateTime;
	
	
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
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
	public BigDecimal getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}
	public BigDecimal getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(BigDecimal orderPrice) {
		this.orderPrice = orderPrice;
	}
	public int getOrderState() {
		return orderState;
	}
	public void setOrderState(int orderState) {
		this.orderState = orderState;
	}
	public int getOrderType() {
		return orderType;
	}
	public void setOrderType(int orderType) {
		this.orderType = orderType;
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
	public BigDecimal getDealCharge() {
		return dealCharge;
	}
	public void setDealCharge(BigDecimal dealCharge) {
		this.dealCharge = dealCharge;
	}
	public Timestamp getOrderCreateTime() {
		return orderCreateTime;
	}
	public void setOrderCreateTime(Timestamp orderCreateTime) {
		this.orderCreateTime = orderCreateTime;
	}
	public Timestamp getOrderCancelTime() {
		return orderCancelTime;
	}
	public void setOrderCancelTime(Timestamp orderCancelTime) {
		this.orderCancelTime = orderCancelTime;
	}
	public Timestamp getEndStateTime() {
		return endStateTime;
	}
	public void setEndStateTime(Timestamp endStateTime) {
		this.endStateTime = endStateTime;
	}
	
	
}

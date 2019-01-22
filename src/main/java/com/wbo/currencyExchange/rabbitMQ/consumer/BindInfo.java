package com.wbo.currencyExchange.rabbitMQ.consumer;

public class BindInfo {

	private String exchange;
	// exchange路由类型
	private String type;
	private String queue;
	private String key;
	
	
	
	
	public String getExchange() {
		return exchange;
	}
	public String getQueue() {
		return queue;
	}
	public String getKey() {
		return key;
	}
	public String getType() {
		return type;
	}
}

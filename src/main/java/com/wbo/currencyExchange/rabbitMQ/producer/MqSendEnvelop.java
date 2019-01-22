package com.wbo.currencyExchange.rabbitMQ.producer;

public class MqSendEnvelop {

	private String exchange;
	private String routingKey;
	
//	public static MqSendEnvelop 
	
	private MqSendEnvelop(String exchange, String routingKey) {
		this.exchange = exchange;
		this.routingKey = routingKey;
	}
	
	public String getExchange() {
		return exchange;
	}
	public String getRoutingKey() {
		return routingKey;
	}
	
	
}

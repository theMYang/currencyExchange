package com.wbo.currencyExchange.rabbitMQ.producer;

public class BaseMqSendEnvelop implements MqSendEnvelopItfc{

	private String exchange;
	private String routingKey;
	
//	public static MqSendEnvelop 
	
	protected BaseMqSendEnvelop(String exchange, String routingKey) {
		this.exchange = exchange;
		this.routingKey = routingKey;
	}
	
	public String getExchange() {
		String clazz = this.getClass().getSimpleName();  
		return clazz + "." + exchange;
	}
	
	public String getRoutingKey() {
		return routingKey;
	}
	
	
}

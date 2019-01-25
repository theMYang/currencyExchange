package com.wbo.currencyExchange.rabbitMQ.producer;

public interface MqSendEnvelopItfc {

	public String getExchange();
	
	public String getRoutingKey();
}

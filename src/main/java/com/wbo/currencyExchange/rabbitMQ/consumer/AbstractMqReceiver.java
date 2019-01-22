package com.wbo.currencyExchange.rabbitMQ.consumer;

public abstract class AbstractMqReceiver implements MqReceiver{

	private BindInfo bindInfo = getBindInfo();
	
}

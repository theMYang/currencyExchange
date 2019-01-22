package com.wbo.currencyExchange.rabbitMQ.consumer;

import java.util.Map;

import com.rabbitmq.client.AMQP.Channel;

public interface MqReceiver {

	public BindInfo getBindInfo();
	
	public void onMessage(Object payLoad, Class<?> clazz, Channel channel, Map headers);
}

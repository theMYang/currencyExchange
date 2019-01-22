package com.wbo.currencyExchange.rabbitMQ.consumer;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import com.rabbitmq.client.AMQP.Channel;

public class DefaultMqReceiver extends AbstractMqReceiver{

	private BindInfo bindInfo;
	
	public DefaultMqReceiver(BindInfo bindInfo) {
		this.bindInfo = bindInfo;
	}
	
	@Override
	public BindInfo getBindInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	final String queue = bindInfo.getQueue();

	@RabbitListener(bindings=@QueueBinding(
			value = @Queue(value= queue, 
			durable = "true"),
			exchange = @Exchange(value=bindInfo.getExchange(), 
			durable = "true",
			type = bindInfo.getType(),
			ignoreDeclarationExceptions = "true"),
			key = bindInfo.getKey()
			)
	)
	@Override
	public void onMessage(Object payLoad, Class<?> clazz, Channel channel, Map headers) {
		// TODO Auto-generated method stub
		
	}
}

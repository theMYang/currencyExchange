package com.wbo.currencyExchange.rabbitMQ.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;

public abstract class AbstractMqSender implements MqSender{
	
	final ConfirmCallback confirmCallback = getConfirmCallback();

	final ReturnCallback returnCallback = getReturnCallback();

}

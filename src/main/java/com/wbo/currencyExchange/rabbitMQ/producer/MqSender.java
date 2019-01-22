package com.wbo.currencyExchange.rabbitMQ.producer;

import org.apache.tomcat.util.net.AprEndpoint.Sendfile;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;

public interface MqSender {

	public ConfirmCallback getConfirmCallback() ;
	
	public ReturnCallback getReturnCallback() ;
	
	public void send(MqSendEnvelop mqSendEnvelop, Object payLoad);

}

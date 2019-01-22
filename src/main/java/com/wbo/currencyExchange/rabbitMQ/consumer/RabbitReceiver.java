package com.wbo.currencyExchange.rabbitMQ.consumer;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
import com.wbo.currencyExchange.domain.UserLogin;



@Component
public class RabbitReceiver {

	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value = "queue-my",
			durable = "true"),
			exchange = @Exchange(value = "exchange-fm",
			durable = "true",
			type = "topic",
			ignoreDeclarationExceptions = "true"),
			key = "wbo.*"
			)
	)
	
	@RabbitHandler
	public void onMessage(Message message, Channel channel) throws Exception {
		System.err.println("--------------------------------------");
		System.err.println("消费端Payload: " + message.getPayload());
		Long deliveryTag = (Long)message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
		//手工ACK
		channel.basicAck(deliveryTag, false);
	}
	
	
	/**
	 * spring.rabbitmq.listener.userLogin.queue.name=queueFM
		spring.rabbitmq.listener.userLogin.queue.durable=true
		spring.rabbitmq.listener.userLogin.exchange.name=exchangeFM
		spring.rabbitmq.listener.userLogin.exchange.durable=true
		spring.rabbitmq.listener.userLogin.exchange.type=topic
		spring.rabbitmq.listener.userLogin.exchange.ignoreDeclarationExceptions=true
		spring.rabbitmq.listener.userLogin.key=wbo.*
	 * @param message
	 * @param channel
	 * @throws Exception
	 */
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value = "${spring.rabbitmq.listener.userLogin.queue.name}",
			durable = "true"),
			exchange = @Exchange(value = "${spring.rabbitmq.listener.userLogin.exchange.name}",
			durable = "true",
			type = "${spring.rabbitmq.listener.userLogin.exchange.type}",
			ignoreDeclarationExceptions = "true"),
			key = "${spring.rabbitmq.listener.userLogin.key}"
			)
	)
	@RabbitHandler
	public void onUserMessage(@Payload UserLogin user, Channel channel,
			@Headers Map<String, Object> headers) throws Exception {
		System.err.println("--------------------------------------");
		System.err.println("消费端user: " + user);
		Long deliveryTag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
		System.err.println("deliveryTag: " + deliveryTag);
		//手工ACK
		// channel.basicAck(deliveryTag, false);
		channel.basicNack(deliveryTag, false, false);
	}
	
	
//	@Autowired
//	private RabbitTemplate rabbitTemplate;
//	
//	public void onUserMessage() throws Exception {
//		UserLogin user = (UserLogin) rabbitTemplate.receiveAndConvert("queueFM");
//		System.err.println(user);
//		//channel.basicNack(deliveryTag, false, false);
//	}
}

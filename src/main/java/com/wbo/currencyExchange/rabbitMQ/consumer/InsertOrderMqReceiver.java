package com.wbo.currencyExchange.rabbitMQ.consumer;

import java.io.IOException;
import java.util.Map;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
import com.wbo.currencyExchange.domain.Order;
import com.wbo.currencyExchange.rabbitMQ.producer.OrderMqSendEnvelop;
import com.wbo.currencyExchange.service.OrderService.PlaceOrderService;

@Component
public class InsertOrderMqReceiver {

	@Autowired
	PlaceOrderService placeOrderService;
	
	@RabbitListener(bindings=@QueueBinding(
			value = @Queue(value= OrderMqSendEnvelop.ORDER_FOR_INSERT_QUEUE_WITH_PREFIX, 
			durable = "true"),
			exchange = @Exchange(value=OrderMqSendEnvelop.ORDER_FOR_INSERT_EXCHANGE_WITH_PREFIX, 
			durable = "true",
			type = "topic",
			ignoreDeclarationExceptions = "true"),
			key = OrderMqSendEnvelop.ORDER_FOR_INSERT_KEY+".#"
			)
	)
	@RabbitHandler
	public void onInsertOrderMessage(@Payload Order order, @Headers Map<String, Object> headers, Channel channel) throws IOException {
		Long deliveryTag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
		
		//mq发过来了userBalance的freezeAmount和userId属性
		boolean res = placeOrderService.insertOrder(order);
		
		//手工 ACK
		if(res) {
			channel.basicAck(deliveryTag, false);
		}else {
			channel.basicNack(deliveryTag, false, true);
		}
		
	}
}

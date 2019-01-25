package com.wbo.currencyExchange.rabbitMQ.consumer;

import java.io.IOException;
import java.math.BigDecimal;
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
import com.wbo.currencyExchange.domain.UserBalance;
import com.wbo.currencyExchange.rabbitMQ.producer.BalanceMqSendEnvelop;
import com.wbo.currencyExchange.service.userService.UserBalanceService;

@Component
public class FreezeBalanceMqReceiver {

	@Autowired
	UserBalanceService userBalanceService;
	
	@RabbitListener(bindings=@QueueBinding(
			value = @Queue(value= BalanceMqSendEnvelop.BALANCE_FOR_ORDER_QUEUE_WITH_PREFIX, 
			durable = "true"),
			exchange = @Exchange(value=BalanceMqSendEnvelop.BALANCE_FOR_ORDER_EXCHANGE_WITH_PREFIX, 
			durable = "true",
			type = "topic",
			ignoreDeclarationExceptions = "true"),
			key = BalanceMqSendEnvelop.BALANCE_FOR_ORDER_KEY+".#"
			)
	)
	@RabbitHandler
	public void onFreezeBalanceMessage(@Payload UserBalance userBalance, @Headers Map<String, Object> headers, Channel channel) throws IOException {
		Long deliveryTag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
		int userId = userBalance.getUserId();
		BigDecimal freezeBalance = userBalance.getFreezeAmount();
		
		boolean res = userBalanceService.freezeBalanceForOrderDB(userBalance);
		
		//手工 ACK
		if(res) {
			channel.basicAck(deliveryTag, false);
		}else {
			channel.basicNack(deliveryTag, false, true);
		}
		
	}
}

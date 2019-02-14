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
import com.wbo.currencyExchange.domain.UserAsset;
import com.wbo.currencyExchange.rabbitMQ.producer.AssetMqSendEnvelop;
import com.wbo.currencyExchange.service.userService.UserAssetService;

@Component
public class FreezeAssetMqReceiver {

	@Autowired
	UserAssetService userAssetService;
	
	@RabbitListener(bindings=@QueueBinding(
			value = @Queue(value= AssetMqSendEnvelop.ASSET_FOR_ORDER_QUEUE_WITH_PREFIX, 
			durable = "true"),
			exchange = @Exchange(value=AssetMqSendEnvelop.ASSET_FOR_ORDER_EXCHANGE_WITH_PREFIX, 
			durable = "true",
			type = "topic",
			ignoreDeclarationExceptions = "true"),
			key = AssetMqSendEnvelop.ASSET_FOR_ORDER_KEY+".#"
			)
	)
	@RabbitHandler
	public void onFreezeAssetMessage(@Payload UserAsset userAsset, @Headers Map<String, Object> headers, Channel channel) throws IOException {
		Long deliveryTag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
		
		//mq发过来了userAsset的freezeAmount、currencyId和userId属性
		boolean res = userAssetService.freezeAssertForOrderDB(userAsset);
		
		//手工 ACK
		if(res) {
			channel.basicAck(deliveryTag, false);
		}else {
			channel.basicNack(deliveryTag, false, true);
		}
		
	}
}

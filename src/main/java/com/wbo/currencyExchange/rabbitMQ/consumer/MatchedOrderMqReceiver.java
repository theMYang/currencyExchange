package com.wbo.currencyExchange.rabbitMQ.consumer;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
import com.wbo.currencyExchange.annotation.ValidMatchedOrder;
import com.wbo.currencyExchange.domain.Order;
import com.wbo.currencyExchange.rabbitMQ.producer.MatchedOrderMqSendEnvelop;
import com.wbo.currencyExchange.service.clearingService.ClearingSystemService;

@Component
public class MatchedOrderMqReceiver {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	ClearingSystemService clearingSystemService;
	
	@RabbitListener(bindings=@QueueBinding(
			value = @Queue(value= MatchedOrderMqSendEnvelop.QUEUE_WITH_PREFIX, 
			durable = "true"),
			exchange = @Exchange(value=MatchedOrderMqSendEnvelop.MQ_EXCHANGE_WITH_PREFIX, 
			durable = "true",
			type = "topic",
			ignoreDeclarationExceptions = "true"),
			key = MatchedOrderMqSendEnvelop.KEY+".#"
			)
	)
	@RabbitHandler
	public void onMatchedOrderMessage(@Payload List<Order> orderGroup, @Headers Map<String, Object> headers, Channel channel) throws IOException {
		Long deliveryTag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
		
		if(orderGroup.size() != 2)
			throw new IllegalArgumentException("撮合买卖订单数非法");
		
		
		Order buyOrder = orderGroup.get(0);
		Order sellOrder = orderGroup.get(1);
		
		if((buyOrder.getOrderType() == Order.SELL_ORDER_TYPE && sellOrder.getOrderType() == Order.BUY_ORDER_TYPE) && 
				buyOrder.getCurrencyId() == sellOrder.getCurrencyId()) {
			Order tmp = sellOrder;
			sellOrder = buyOrder;
			buyOrder = tmp;
		}else if(buyOrder.getOrderType() == sellOrder.getOrderType()){
//			logger.error("orderGroup[0] order type is: "+ orderGroup[0].getOrderType()+"," + "orderGroup[1] order type is: "+ orderGroup[1].getOrderType());
			throw new IllegalArgumentException("撮合订单类型非法");
		}
		
//		//mq发过来了撮合系统的买、卖订单
//		boolean res = clearingSystemService.clearingOrder(buyOrder, sellOrder);
//		
//		//手工 ACK
//		if(res) {
//			channel.basicAck(deliveryTag, false);
//		}else {
//			channel.basicNack(deliveryTag, false, true);
//		}
		
		try {
			logger.info("清算MQ买"+buyOrder);
			logger.info("清算MQ卖"+sellOrder);
			
			boolean res = clearingSystemService.clearingOrder(buyOrder, sellOrder);
			if(res) {
				channel.basicAck(deliveryTag, false);
			}else {
				channel.basicNack(deliveryTag, false, true);
			}
		} catch (Exception e) {
			channel.basicNack(deliveryTag, false, true);
			// 并且将buyOrder, sellOrder重回队列
		}
		
		
	}
}

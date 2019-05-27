package com.wbo.currencyExchange.rabbitMQ.producer;


public class OrderMqSendEnvelop extends BaseMqSendEnvelop {

	private OrderMqSendEnvelop(String exchange, String routingKey) {
		super(exchange, routingKey);
	}
	
	public static final String CLAZZ_PREFIX = "OrderMqSendEnvelop";
	
	public static final String ORDER_FOR_INSERT_EXCHANGE = "orderInsert";
	public static final String ORDER_FOR_INSERT_EXCHANGE_WITH_PREFIX = CLAZZ_PREFIX+"."+ORDER_FOR_INSERT_EXCHANGE;
	public static final String ORDER_FOR_INSERT_QUEUE_WITH_PREFIX = CLAZZ_PREFIX+"."+ORDER_FOR_INSERT_EXCHANGE;
	public static final String ORDER_FOR_INSERT_KEY = "order";
	
	public static final OrderMqSendEnvelop ORDER_FOR_INSERT_MQ = new OrderMqSendEnvelop(ORDER_FOR_INSERT_EXCHANGE, 
			ORDER_FOR_INSERT_KEY+".insert");
	
}

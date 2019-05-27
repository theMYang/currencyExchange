package com.wbo.currencyExchange.rabbitMQ.producer;

// 撮合好的订单，传给清算系统
public class MatchedOrderMqSendEnvelop extends BaseMqSendEnvelop {

	private MatchedOrderMqSendEnvelop(String exchange, String routingKey) {
		super(exchange, routingKey);
	}
	
	public static final String CLAZZ_PREFIX = "MatchedOrderMqSendEnvelop";
	
	public static final String MQ_EXCHANGE = "matchedOrder";
	public static final String MQ_EXCHANGE_WITH_PREFIX = CLAZZ_PREFIX+"."+MQ_EXCHANGE;
	public static final String QUEUE_WITH_PREFIX = CLAZZ_PREFIX+"."+MQ_EXCHANGE;
	public static final String KEY = "order";
	
	public static final MatchedOrderMqSendEnvelop MATCHED_ORDER_MQ = new MatchedOrderMqSendEnvelop(MQ_EXCHANGE, KEY+".matched");
	
}

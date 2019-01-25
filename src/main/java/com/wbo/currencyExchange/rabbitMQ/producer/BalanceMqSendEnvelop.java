package com.wbo.currencyExchange.rabbitMQ.producer;

public class BalanceMqSendEnvelop extends BaseMqSendEnvelop {

	
	private BalanceMqSendEnvelop(String exchange, String routingKey) {
		super(exchange, routingKey);
	}
	
	public static final String CLAZZ_PREFIX = "BalanceMqSendEnvelop";
	
	public static final String BALANCE_FOR_ORDER_EXCHANGE = "balanceOrder";
	public static final String BALANCE_FOR_ORDER_EXCHANGE_WITH_PREFIX = CLAZZ_PREFIX+"."+BALANCE_FOR_ORDER_EXCHANGE;
	public static final String BALANCE_FOR_ORDER_QUEUE_WITH_PREFIX = CLAZZ_PREFIX+"."+BALANCE_FOR_ORDER_EXCHANGE;
	public static final String BALANCE_FOR_ORDER_KEY = "balance";
	
	public static final BalanceMqSendEnvelop BALANCE_FOR_ORDER_MQ = new BalanceMqSendEnvelop(BALANCE_FOR_ORDER_EXCHANGE, 
			BALANCE_FOR_ORDER_KEY+".freeze");
}

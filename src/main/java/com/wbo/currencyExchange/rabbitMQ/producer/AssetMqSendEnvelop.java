package com.wbo.currencyExchange.rabbitMQ.producer;

public class AssetMqSendEnvelop extends BaseMqSendEnvelop  {

	private AssetMqSendEnvelop(String exchange, String routingKey) {
		super(exchange, routingKey);
	}
	
	public static final String CLAZZ_PREFIX = "AssetMqSendEnvelop";
	
	public static final String ASSET_FOR_ORDER_EXCHANGE = "assetOrder";
	public static final String ASSET_FOR_ORDER_EXCHANGE_WITH_PREFIX = CLAZZ_PREFIX+"."+ASSET_FOR_ORDER_EXCHANGE;
	public static final String ASSET_FOR_ORDER_QUEUE_WITH_PREFIX = CLAZZ_PREFIX+"."+ASSET_FOR_ORDER_EXCHANGE;
	public static final String ASSET_FOR_ORDER_KEY = "asset";
	
	public static final AssetMqSendEnvelop ASSET_FREEZE_CURRENCY_MQ = new AssetMqSendEnvelop(ASSET_FOR_ORDER_EXCHANGE, 
			ASSET_FOR_ORDER_KEY+".freeze");
	
}

package com.wbo.currencyExchange.rabbitMQ.producer;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.beans.factory.annotation.Autowired;

import com.wbo.currencyExchange.exception.GlobalException;
import com.wbo.currencyExchange.result.CodeMsg;
import com.wbo.currencyExchange.util.UUIDUtil;

public class DefaultMqSender extends AbstractMqSender {

	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Override
	public ConfirmCallback getConfirmCallback() {
		
		ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
			
			@Override
			public void confirm(CorrelationData correlationData, boolean ack, String cause) {
				if(!ack){
					String errorMsg = "correlationData:"+ correlationData;
					throw new GlobalException(CodeMsg.MQ_CONFIRM_ERROR.fillArgs(errorMsg));
				}
			}
		};
		return confirmCallback;
	}

	@Override
	public ReturnCallback getReturnCallback() {
		
		ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
			
			@Override
			public void returnedMessage(org.springframework.amqp.core.Message message, int replyCode, String replyText,
					String exchange, String routingKey) {
				String errorMsg = "return exchange: " + exchange + ", routingKey: " 
						+ routingKey + ", replyCode: " + replyCode + ", replyText: " + replyText;
				throw new GlobalException(CodeMsg.MQ_RETURN_ERROR.fillArgs(errorMsg));
			}
		};
		return returnCallback;
	}
	
	@Override
	public void send(MqSendEnvelop mqSendEnvelop, Object payLoad) {
		rabbitTemplate.setConfirmCallback(confirmCallback);
		rabbitTemplate.setReturnCallback(returnCallback);
		
		String uuid = UUIDUtil.uuid();
		CorrelationData correlationData = new CorrelationData(uuid);
		rabbitTemplate.convertAndSend(mqSendEnvelop.getExchange(), mqSendEnvelop.getRoutingKey(), payLoad, correlationData);
	}
	
}

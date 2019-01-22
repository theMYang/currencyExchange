package com.wbo.currencyExchange;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.wbo.currencyExchange.rabbitMQ.consumer.RabbitReceiver;

@RunWith(SpringRunner.class)
@SpringBootTest
public class rabbitMQReceiverTest {
	
	@Autowired
	RabbitReceiver rabbitReceiver;
	
	@Test
	public void testRabbitConsumerObj() throws Exception {
		rabbitReceiver.onUserMessage();
	}
}

package com.wbo.currencyExchange;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.wbo.currencyExchange.domain.UserLogin;
import com.wbo.currencyExchange.rabbitMQ.producer.RabbitSender;

@RunWith(SpringRunner.class)
@SpringBootTest
public class rabbitMQProducerTest {

	private static Logger logger = LogManager.getLogger(rabbitMQProducerTest.class);
	
	@Test
	public void contextLoads() {
	}
	
	@Autowired
	RabbitSender rabbitSender;
	
//	@Test
//	public void testRabbitProducer() throws Exception {
//		Map<String, Object> properties = new HashMap<>();
//		properties.put("number", "12345");
//		 properties.put("send_time", "2019-01-12");
//		rabbitSender.send("message send", properties);
//	}
	
	@Test
	public void testRabbitProducerObj() throws Exception {
		Map<String, Object> properties = new HashMap<>();
		UserLogin user = new UserLogin();
		 user.setLoginName("测不准");
		 user.setMobilePhone("1309696");
		 user.setPassword("1416");
		 user.setUserId(123);
		 user.setUserState(1);
		rabbitSender.sendUser(user);
	}
}

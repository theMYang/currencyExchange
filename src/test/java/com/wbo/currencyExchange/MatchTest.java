package com.wbo.currencyExchange;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.wbo.currencyExchange.annotation.ValidMatchedOrder;
import com.wbo.currencyExchange.domain.MatchSequence;
import com.wbo.currencyExchange.domain.Order;
import com.wbo.currencyExchange.domain.OrderDriven;
import com.wbo.currencyExchange.service.matchService.SequenceService;
import com.wbo.currencyExchange.serviceImpl.matchServiceImpl.SequenceServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
@PropertySource("classpath:/sysconfig.properties")
public class MatchTest {
	
	private static Logger logger = LogManager.getLogger(MatchTest.class);
	
	@Autowired
	SequenceServiceImpl sequenceService;
	@Autowired
	MatchSequence matchSequence;
	@Value("${matchSystem.ThreadNum}")
	int val =5;
	
	@Test
	public void initSequenceTest() {
		
		sequenceService.initSequence();
		ConcurrentHashMap<Integer, OrderDriven> map = matchSequence.getSequenceMap();
		System.err.println(map);
	}
	
}

package com.wbo.currencyExchange;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.wbo.currencyExchange.service.matchService.SequenceService;
import com.wbo.currencyExchange.serviceImpl.matchServiceImpl.SequenceServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MatchTest {

	private static Logger logger = LogManager.getLogger(MatchTest.class);
	
	@Autowired
	SequenceServiceImpl sequenceService;
	
	@Test
	public void initSequenceTest() {
		sequenceService.initSequence();
	}
}

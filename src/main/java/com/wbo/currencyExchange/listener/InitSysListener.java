package com.wbo.currencyExchange.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.wbo.currencyExchange.service.matchService.SequenceService;

@Component
public class InitSysListener {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	SequenceService sequenceService;
	
	@EventListener(classes= {ContextRefreshedEvent.class})
	public void initSequenceForBoot() {
		logger.warn("监听refresh，对撮合队列进行初始化");
		sequenceService.initSequence();
	}
	
	
}

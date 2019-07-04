package com.wbo.currencyExchange.domain;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class MatchSequence {

	private static final ConcurrentHashMap<Integer, OrderDriven> sequenceMap = new ConcurrentHashMap<Integer, OrderDriven>();
	
	
	public ConcurrentHashMap<Integer, OrderDriven> getSequenceMap() {
		return sequenceMap;
	}

	
//	public void setSequenceMap(ConcurrentHashMap<Integer, OrderDriven> sequenceMap) {
//		this.sequenceMap = new ConcurrentHashMap<Integer, OrderDriven>(sequenceMap);
//	}
	
	
}

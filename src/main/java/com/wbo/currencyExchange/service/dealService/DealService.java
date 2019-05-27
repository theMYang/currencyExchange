package com.wbo.currencyExchange.service.dealService;

import org.springframework.stereotype.Service;

import com.wbo.currencyExchange.domain.Deal;


@Service
public interface DealService {

	public boolean insertDeal(Deal deal);
}

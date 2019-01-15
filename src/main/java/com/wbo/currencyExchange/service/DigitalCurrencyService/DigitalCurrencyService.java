package com.wbo.currencyExchange.service.DigitalCurrencyService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wbo.currencyExchange.domain.DigitalCurrency;

@Service
public interface DigitalCurrencyService {
	
	public List<DigitalCurrency> getHomePageCurrency();
	
}

package com.wbo.currencyExchange.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wbo.currencyExchange.dao.DigitalCurrencyDao;
import com.wbo.currencyExchange.domain.DigitalCurrency;
import com.wbo.currencyExchange.service.DigitalCurrencyService;

@Service
public class DigitalCurrencyServiceImpl implements DigitalCurrencyService{
	
	@Autowired
	DigitalCurrencyDao digitalCurrencyDao;
	
	public List<DigitalCurrency> getHomePageCurrency() {
		return digitalCurrencyDao.getHomePageCurrency();
	}
	
}

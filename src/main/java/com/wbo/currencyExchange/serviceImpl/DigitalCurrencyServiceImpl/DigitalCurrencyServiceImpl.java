package com.wbo.currencyExchange.serviceImpl.DigitalCurrencyServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wbo.currencyExchange.dao.DigitalCurrencyDao.DigitalCurrencyDao;
import com.wbo.currencyExchange.domain.DigitalCurrency;
import com.wbo.currencyExchange.service.DigitalCurrencyService.DigitalCurrencyService;

@Service
public class DigitalCurrencyServiceImpl implements DigitalCurrencyService{
	
	@Autowired
	DigitalCurrencyDao digitalCurrencyDao;
	
	public List<DigitalCurrency> getHomePageCurrency() {
		return digitalCurrencyDao.getHomePageCurrency();
	}
	
}

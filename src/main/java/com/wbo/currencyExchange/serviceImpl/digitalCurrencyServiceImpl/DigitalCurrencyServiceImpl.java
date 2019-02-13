package com.wbo.currencyExchange.serviceImpl.digitalCurrencyServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wbo.currencyExchange.dao.digitalCurrencyDao.DigitalCurrencyDao;
import com.wbo.currencyExchange.domain.DigitalCurrency;
import com.wbo.currencyExchange.service.digitalCurrencyService.DigitalCurrencyService;

@Service
public class DigitalCurrencyServiceImpl implements DigitalCurrencyService{
	
	@Autowired
	DigitalCurrencyDao digitalCurrencyDao;
	
	public List<DigitalCurrency> getHomePageCurrency() {
		return digitalCurrencyDao.getHomePageCurrency();
	}

	@Override
	public List<DigitalCurrency> getAllCurrency() {
		return digitalCurrencyDao.getAllCurrency();
	}
	
}

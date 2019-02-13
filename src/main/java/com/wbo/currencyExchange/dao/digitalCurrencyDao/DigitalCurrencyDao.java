package com.wbo.currencyExchange.dao.digitalCurrencyDao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.wbo.currencyExchange.domain.DigitalCurrency;

@Mapper
public interface DigitalCurrencyDao {
	
	public List<DigitalCurrency> getHomePageCurrency();
	
	public List<DigitalCurrency> getAllCurrency();
	
}

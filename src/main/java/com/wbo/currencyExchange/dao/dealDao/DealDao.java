package com.wbo.currencyExchange.dao.dealDao;

import org.apache.ibatis.annotations.Mapper;

import com.wbo.currencyExchange.domain.Deal;

@Mapper
public interface DealDao {

	public int insertDeal(Deal deal);
}

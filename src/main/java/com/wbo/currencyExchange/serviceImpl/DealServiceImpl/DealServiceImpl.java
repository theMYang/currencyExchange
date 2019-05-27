package com.wbo.currencyExchange.serviceImpl.DealServiceImpl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wbo.currencyExchange.dao.dealDao.DealDao;
import com.wbo.currencyExchange.domain.Deal;
import com.wbo.currencyExchange.exception.GlobalException;
import com.wbo.currencyExchange.result.CodeMsg;
import com.wbo.currencyExchange.service.dealService.DealService;

@Service
public class DealServiceImpl implements DealService{

	@Autowired
	DealDao dealDao;
	
	@Override
	public boolean insertDeal(Deal deal) {
		boolean res = false;
		if(!insertDealCheckField(deal)) {
			throw new GlobalException(CodeMsg.DEAL_VAILD_ERROR);
		}
		
		int rows = dealDao.insertDeal(deal);
		if(rows>0) {
			res = true;
		}
		return res;
	}

	private boolean insertDealCheckField(Deal deal) {
		boolean res = true;
		
		res &= deal.getBuyOrderId()>0;
		res &= deal.getSellOrderId()>0;
		res &= deal.getDealAmount().compareTo(BigDecimal.ZERO)>0;
		res &= deal.getDealPrice().compareTo(BigDecimal.ZERO)>0;
		
		return res;
	}

}

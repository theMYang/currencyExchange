package com.wbo.currencyExchange.annotation;

import java.math.BigDecimal;
import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wbo.currencyExchange.domain.Order;
import com.wbo.currencyExchange.exception.GlobalException;
import com.wbo.currencyExchange.result.CodeMsg;

@Aspect
@Component
public class ValidMatchedOrderAspect {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	Order buyOrder;
	@Autowired
	Order sellOrder;
	
	@Pointcut("@annotation(com.wbo.currencyExchange.annotation.ValidMatchedOrder)")
	public void validMatchedOrderAnnotationPointCut() {
		
	}
	
	
	private static final int MATCHED_ORDER_NUM = 2;
	
	@Before("validMatchedOrderAnnotationPointCut()")
	public void beforeValidMatchedOrder(JoinPoint joinPoint) {
		Order[] matchedOrders = new Order[MATCHED_ORDER_NUM];
		Object[] args = joinPoint.getArgs();
		int pos = 0;
		for(Object arg: args) {
			if(arg instanceof Order) {
				matchedOrders[pos++]=(Order) arg;
				if(pos==MATCHED_ORDER_NUM)
					break;
			}
		}
		
		
		if(matchedOrders[0].getOrderType() == matchedOrders[1].getOrderType()) {
			logger.error(joinPoint.getSignature()+ ";"+ "参数列表："+Arrays.asList(joinPoint.getArgs())+ "订单为："+Arrays.asList(matchedOrders));
			throw new GlobalException(CodeMsg.MATCHED_ORDER_TYPE_ERROR);
		}else {
			if(matchedOrders[0].getOrderType()==Order.BUY_ORDER_TYPE) {
				buyOrder = matchedOrders[0];
				sellOrder = matchedOrders[1];
			}else if(matchedOrders[0].getOrderType()==Order.SELL_ORDER_TYPE) {
				buyOrder = matchedOrders[1];
				sellOrder = matchedOrders[0];
			}
		}
		
		if(buyOrder.getUserId() == sellOrder.getUserId()) {
			logger.error(joinPoint.getSignature()+ ";"+ "参数列表："+Arrays.asList(joinPoint.getArgs())+ "订单为："+Arrays.asList(matchedOrders));
			throw new GlobalException(CodeMsg.MATCHED_ORDER_USER_ERROR);
		}
		if(buyOrder.getCurrencyId() != sellOrder.getCurrencyId()) {
			logger.error(joinPoint.getSignature()+ ";"+ "参数列表："+Arrays.asList(joinPoint.getArgs())+ "订单为："+Arrays.asList(matchedOrders));
			throw new GlobalException(CodeMsg.MATCHED_ORDER_CURRENCY_ERROR);
		}
		if(buyOrder.getOrderAmount().compareTo(BigDecimal.ZERO)<=0 && sellOrder.getOrderAmount().compareTo(BigDecimal.ZERO)<=0) {
			logger.error(joinPoint.getSignature()+ ";"+ "参数列表："+Arrays.asList(joinPoint.getArgs())+ "订单为："+Arrays.asList(matchedOrders));
			throw new GlobalException(CodeMsg.MATCHED_ORDER_AMOUNT_ERROR);
		}
		
	}
}

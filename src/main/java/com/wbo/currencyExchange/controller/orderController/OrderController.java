package com.wbo.currencyExchange.controller.orderController;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wbo.currencyExchange.domain.Order;
import com.wbo.currencyExchange.domain.UserLogin;
import com.wbo.currencyExchange.exception.GlobalException;
import com.wbo.currencyExchange.result.CodeMsg;
import com.wbo.currencyExchange.result.ResultCode;
import com.wbo.currencyExchange.service.orderService.PlaceOrderService;

@Controller
@RequestMapping("/order")
@ResponseBody
public class OrderController {

	@Autowired
	PlaceOrderService placeOrder;
	
	//交易类型--限价买
	private static final String orderTypeBuyLimit = "buy-limit";
	//交易类型--限价卖
	private static final String orderTypeSellLimit = "sell-limit";
	
	@RequestMapping("/tradeCurrency")
	public ResultCode tradeCurrency(String amount, String price, String type, int currencyId, UserLogin user) {
		if(amount==null || amount.equals("") )
			throw new GlobalException(CodeMsg.ORDER_AMOUNT_ERROR);
		if(price==null || price.equals("") )
			throw new GlobalException(CodeMsg.ORDER_PRICE_ERROR);
		if(type==null || type.equals("") )
			throw new GlobalException(CodeMsg.ORDER_TYPE_ERROR);
		
		BigDecimal tradeAmount = new BigDecimal(amount);
		BigDecimal tradePrice = new BigDecimal(price);
		
		if(tradeAmount.compareTo(BigDecimal.ZERO) <=0 || tradePrice.compareTo(BigDecimal.ZERO) <=0) {
			throw new GlobalException(CodeMsg.ORDER_VALUE_ERROR);
		}
		
		ResultCode resultCode;
		if(type.equals(orderTypeBuyLimit)) {
			resultCode = placeOrder.placeBuyOrder(tradeAmount, tradePrice, currencyId, user);
		}else if(type.equals(orderTypeSellLimit)) {
			resultCode = placeOrder.placeSellOrder(tradeAmount, tradePrice, currencyId, user);
		}else {
			throw new GlobalException(CodeMsg.ORDER_TYPE_ERROR);
		}
		
		return resultCode;
	}
	
}

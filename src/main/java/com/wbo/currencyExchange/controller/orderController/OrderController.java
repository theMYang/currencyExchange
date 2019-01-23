package com.wbo.currencyExchange.controller.orderController;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wbo.currencyExchange.domain.UserLogin;
import com.wbo.currencyExchange.exception.GlobalException;
import com.wbo.currencyExchange.result.CodeMsg;
import com.wbo.currencyExchange.result.ResultCode;
import com.wbo.currencyExchange.service.OrderService.PlaceOrder;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	PlaceOrder placeOrder;
	
	//交易类型--限价买
	private static final String orderTypeBuyLimit = "buy-limit";
	//交易类型--限价卖
	private static final String orderTypeSellLimit = "sell-limit";
	
	@RequestMapping("/buyCurrency")
	public ResultCode buyCurrency(String amount, String price, String type, UserLogin user) {
		if(amount==null || amount.equals("") )
			throw new GlobalException(CodeMsg.ORDER_AMOUNT_ERROR);
		if(price==null || price.equals("") )
			throw new GlobalException(CodeMsg.ORDER_PRICE_ERROR);
		if(type==null || type.equals("") )
			throw new GlobalException(CodeMsg.ORDER_TYPE_ERROR);
		
		BigDecimal purchaseAmount = new BigDecimal(amount);
		BigDecimal purchasePrice = new BigDecimal(price);
		
		ResultCode resultCode;
		if(type.equals(orderTypeBuyLimit)) {
			resultCode = placeOrder.placeBuyOrder(purchaseAmount, purchasePrice, user);
		}else if(type.equals(orderTypeSellLimit)) {
			resultCode = placeOrder.placeSellOrder(purchaseAmount, purchasePrice, user);
		}else {
			throw new GlobalException(CodeMsg.ORDER_TYPE_ERROR);
		}
		
		return resultCode;
	}
	
}
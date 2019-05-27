package com.wbo.currencyExchange.serviceImpl.clearingServiceImpl;


import java.math.BigDecimal;
import java.sql.Timestamp;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wbo.currencyExchange.domain.Deal;
import com.wbo.currencyExchange.domain.Order;
import com.wbo.currencyExchange.domain.UserAsset;
import com.wbo.currencyExchange.domain.UserBalance;
import com.wbo.currencyExchange.exception.GlobalException;
import com.wbo.currencyExchange.result.CodeMsg;
import com.wbo.currencyExchange.service.clearingService.ClearingSystemService;
import com.wbo.currencyExchange.service.dealService.DealService;
import com.wbo.currencyExchange.service.matchService.SequenceService;
import com.wbo.currencyExchange.service.orderService.PlaceOrderService;
import com.wbo.currencyExchange.service.userService.UserAssetService;
import com.wbo.currencyExchange.service.userService.UserBalanceService;



@Service
@Scope(value="prototype")
public class ClearingSystemServiceImpl implements ClearingSystemService, ApplicationContextAware {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	Deal deal;
	@Autowired
	PlaceOrderService placeOrderService;
	@Autowired
	DealService dealService;
	@Autowired
	Order restOrder;
	@Autowired
	SequenceService sequenceService;
	
	
	
	/**
	 * 
	 * 为了解决同一个类中调另一个函数没法用@Transactional，clearingOrder调用clearingBalanceNAsset。在clearingOrder中@Autowired	ClearingSystemService ，
	 * 从beanFactory中取出clearingBalanceNAsset调用，解决该问题。
	 * 之后联调撮合系统后，发现了线程问题。
	 * 解决方法1：把消息队列的并发参数（concurrency）改为1，改成个单线程访问。这种解决不彻底。
	 * 解决方法2：把该类scop改为property。但由于@Autowired	ClearingSystemService会造成循环依赖的问题。去掉这个autowired，改用ApplicationContextAware之后从beanFactory
	 * 					手动取出这个类。
	 * 
	 * 
	 * 
	 */
//	@Autowired
//	ClearingSystemService clearingSystemService;
	
	@Override
	@Transactional
	public boolean clearingOrder(Order matchedBuyOrder,  Order matchedSellOrder) {
		// 校验order正确性
		boolean vaildRes = vaildMatchedOrder(matchedBuyOrder, matchedSellOrder);
		if(!vaildRes) {
			throw new GlobalException(CodeMsg.CLEARING_ORDER_VAILD_ERROR);
		}
		
		// 总订单量
		BigDecimal buyOrderTotalAmount = matchedBuyOrder.getOrderAmount();
		BigDecimal sellOrderTotalAmount = matchedSellOrder.getOrderAmount();

		// 本次成交量
		BigDecimal dealAmount = dealAmount(matchedBuyOrder,  matchedSellOrder) ;
		
		
		// 本次成交价
		BigDecimal dealPrice = dealPrice(matchedBuyOrder, matchedSellOrder);
		
		// 成交总价
		BigDecimal orderPriceTotal = matchedOrderTotalDealPrice(matchedBuyOrder, matchedSellOrder) ;
		
		//待更新已成交量
		matchedBuyOrder.setDealAmount(matchedBuyOrder.getDealAmount().add(dealAmount));
		matchedSellOrder.setDealAmount(matchedSellOrder.getDealAmount().add(dealAmount));
		//待更新已成交金额
		matchedBuyOrder.setDealPrice(matchedBuyOrder.getDealPrice().add(orderPriceTotal));
		matchedSellOrder.setDealPrice(matchedSellOrder.getDealPrice().add(orderPriceTotal));
		
		// 初始化为部分提交
		matchedBuyOrder.setOrderState(Order.PARTIAL_COMMITED);
		matchedSellOrder.setOrderState(Order.PARTIAL_COMMITED);
		
		// 判断是否完全成交
		if(buyOrderTotalAmount.compareTo(matchedBuyOrder.getDealAmount()) == 0) {
			matchedBuyOrder.setOrderState(Order.DEAL);
			matchedBuyOrder.setEndStateTime(new Timestamp(System.currentTimeMillis()));
		}
		if(sellOrderTotalAmount.compareTo(matchedSellOrder.getDealAmount()) == 0) {
			matchedSellOrder.setOrderState(Order.DEAL);
			matchedSellOrder.setEndStateTime(new Timestamp(System.currentTimeMillis()));
		}
		
		
		// 构造deal对象
		deal.setBuyOrderId(matchedBuyOrder.getOrderId());
		deal.setSellOrderId(matchedSellOrder.getOrderId());
		deal.setDealAmount(dealAmount);
		deal.setDealPrice(dealPrice);

		updateClearingResult(matchedBuyOrder, matchedSellOrder, deal, dealAmount, orderPriceTotal);
		
//		clearingSystemService.clearingBalanceNAsset(matchedBuyOrder, matchedSellOrder, dealAmount, orderPriceTotal);
		ClearingSystemService clearingSystemService = applicationContext.getBean(ClearingSystemService.class);
		clearingSystemService.clearingBalanceNAsset(matchedBuyOrder, matchedSellOrder, dealAmount, orderPriceTotal);
		
		return true;
	}
	
	
	// 更新Order和Deal
	@Transactional
	public void updateClearingResult(Order matchedBuyOrder, Order matchedSellOrder, Deal deal, BigDecimal dealAmount, BigDecimal orderPriceTotal) {
		boolean insertDealSucc = dealService.insertDeal(deal);
		if(!insertDealSucc)
			throw new GlobalException(CodeMsg.INSERT_DEAL_ERROR);
		
		int num = placeOrderService.updateOrderForClearing(matchedBuyOrder);
		if(num<1) {
			throw new GlobalException(CodeMsg.CLEARING_ORDER_UPDATE_ERROR);
		}
		
		num = placeOrderService.updateOrderForClearing(matchedSellOrder);
		if(num<1) {
			throw new GlobalException(CodeMsg.CLEARING_ORDER_UPDATE_ERROR);
		}
		
		reAddResrOrderToSeq(matchedBuyOrder, matchedSellOrder);
	}
	
	

	@Autowired
	UserBalance clearingBuyBalance;
	@Autowired
	UserBalance clearingSellBalance;
	@Autowired
	UserAsset clearingBuyAsset;
	@Autowired
	UserAsset clearingSellAsset;
	@Autowired
	UserBalanceService userBalanceService;
	@Autowired
	UserAssetService userAssetService;

	// 结算余额和资产
	@Transactional
	public void clearingBalanceNAsset(Order matchedBuyOrder, Order matchedSellOrder, BigDecimal dealAmount, BigDecimal dealPrice) {

		boolean vaildRes = vaildMatchedOrder(matchedBuyOrder, matchedSellOrder);
		if(!vaildRes) {
			throw new GlobalException(CodeMsg.CLEARING_ORDER_VAILD_ERROR);
		}
		
		// 对于买家， 减balance加asset
		clearingBuyBalance.setUserId(matchedBuyOrder.getUserId());
		
		// 之前冻结资金解冻
		BigDecimal frozenFunds = matchedBuyOrder.getOrderAmount().multiply(matchedBuyOrder.getOrderPrice());
		// 将之前冻结资金解冻
		clearingBuyBalance.setFreezeAmount(frozenFunds.negate());
		// 减去花费
		clearingBuyBalance.setBalanceAmount(dealPrice.negate());
		
		
		clearingBuyAsset.setUserId(matchedBuyOrder.getUserId());
		clearingBuyAsset.setCurrencyId(matchedBuyOrder.getCurrencyId());
		clearingBuyAsset.setCurrencyAmount(dealAmount);
		
		
		// 卖家，加balance减asset
		clearingSellBalance.setUserId(matchedSellOrder.getUserId());
		clearingSellBalance.setBalanceAmount(dealPrice);
		
		clearingSellAsset.setUserId(matchedSellOrder.getUserId());
		clearingSellAsset.setCurrencyId(matchedSellOrder.getCurrencyId());
		clearingSellAsset.setCurrencyAmount(dealAmount.negate());
		clearingSellAsset.setFreezeAmount(dealAmount.negate());
		
		
		if(!userBalanceService.updateBalanceForClearing(clearingBuyBalance)) {
			throw new GlobalException(CodeMsg.CLEARING_PROPERTY_UPDATE_ERROR);
		}
		
		if(!userBalanceService.updateBalanceForClearing(clearingSellBalance)) {
			throw new GlobalException(CodeMsg.CLEARING_PROPERTY_UPDATE_ERROR);
		}
		
		if(!userAssetService.updateAssertForClearing(clearingBuyAsset)) {
			throw new GlobalException(CodeMsg.CLEARING_PROPERTY_UPDATE_ERROR);
		}
		
		if(!userAssetService.updateAssertForClearing(clearingSellAsset)) {
			throw new GlobalException(CodeMsg.CLEARING_PROPERTY_UPDATE_ERROR);
		}
		
	}
	
	
	
	
	
	// 将匹配后有剩余的订单重回定序队列
	public void reAddResrOrderToSeq(Order matchedBuyOrder, Order matchedSellOrder) {
		
		if(matchedBuyOrder==null || matchedSellOrder==null)
			throw new GlobalException(CodeMsg.CLEARING_ORDER_VAILD_ERROR);

		BigDecimal restBuyOrderAmount = matchedBuyOrder.getOrderAmount().subtract(matchedBuyOrder.getDealAmount());
		BigDecimal restSellOrderAmount = matchedSellOrder.getOrderAmount().subtract(matchedSellOrder.getDealAmount());
		
		boolean restBuyOrderAmountBigThenZero = BigDecimal.ZERO.compareTo(restBuyOrderAmount) <0;
		boolean restSellOrderAmountBigThenZero = BigDecimal.ZERO.compareTo(restSellOrderAmount) <0;
		boolean restBuyOrderAmountLessThenZero = BigDecimal.ZERO.compareTo(restBuyOrderAmount) >0;
		boolean restSellOrderAmountLessThenZero = BigDecimal.ZERO.compareTo(restSellOrderAmount) >0;
		boolean restBuyOrderAmountEqualThenZero = BigDecimal.ZERO.compareTo(restBuyOrderAmount) <0;
		boolean restSellOrderAmountEqualThenZero = BigDecimal.ZERO.compareTo(restSellOrderAmount) <0;
		
		// 不可能匹配后买卖订单剩余都大于0
		if(restBuyOrderAmountBigThenZero && restSellOrderAmountBigThenZero) {
			throw new GlobalException(CodeMsg.CLEARING_ORDER_UPDATE_ERROR);
		}
		
		if(restBuyOrderAmountEqualThenZero && restSellOrderAmountEqualThenZero) {
			return;
		}
		
		if(restBuyOrderAmountLessThenZero) {
			throw new GlobalException(CodeMsg.OVERSOLD_ERROR);
		}else if(restBuyOrderAmountBigThenZero) {
			restOrder = matchedBuyOrder;
		}
		
		if(restSellOrderAmountLessThenZero) {
			throw new GlobalException(CodeMsg.OVERSOLD_ERROR);
		}else if(restSellOrderAmountBigThenZero) {
			restOrder = matchedSellOrder;
		}
		
		if(!sequenceService.placeOrderToSequence(restOrder)) {
			throw new GlobalException(CodeMsg.ADD_TO_SEQUENCE_ERROR);
		}
		
	}

	// 成交量
		private BigDecimal dealAmount(Order matchedBuyOrder, Order matchedSellOrder) {
			// 总订单量
			BigDecimal buyOrderTotalAmount = matchedBuyOrder.getOrderAmount();
			BigDecimal sellOrderTotalAmount = matchedSellOrder.getOrderAmount();
			
			// 本次成交量（OrderAmount不会变）
			// 购买量
			BigDecimal buyOrderAmount = buyOrderTotalAmount.subtract(matchedBuyOrder.getDealAmount());
			if(BigDecimal.ZERO.compareTo(buyOrderAmount) == 1) {
				throw new GlobalException(CodeMsg.OVERSOLD_ERROR);
			}
			// 卖出量
			BigDecimal sellOrderAmount = sellOrderTotalAmount.subtract(matchedSellOrder.getDealAmount());
			if(BigDecimal.ZERO.compareTo(sellOrderAmount) == 1) {
				throw new GlobalException(CodeMsg.OVERSOLD_ERROR);
			}
			BigDecimal dealAmount = buyOrderAmount.min(sellOrderAmount);
			return dealAmount;
		}
		
		
		
		
		// 成交价
		private BigDecimal dealPrice(Order matchedBuyOrder, Order matchedSellOrder) {
			BigDecimal buyOrderPrice = matchedBuyOrder.getOrderPrice();
			BigDecimal sellOrderPrice = matchedSellOrder.getOrderPrice();
			BigDecimal dealPrice = buyOrderPrice.add(sellOrderPrice).divide(new BigDecimal(2), 6, BigDecimal.ROUND_HALF_UP);
			return dealPrice;
		}
		
		
		private BigDecimal matchedOrderTotalDealPrice(Order matchedBuyOrder, Order matchedSellOrder) {
			// 本次成交量
			BigDecimal dealAmount = dealAmount(matchedBuyOrder,  matchedSellOrder) ;
			// 本次成交价
			BigDecimal dealPrice = dealPrice(matchedBuyOrder, matchedSellOrder);
			// 成交总价
			BigDecimal orderPriceTotal = dealAmount.multiply(dealPrice);
			return orderPriceTotal;
		}
		

	//待完成
	private boolean vaildMatchedOrder(Order matchedBuyOrder,  Order matchedSellOrder) {
		return true;
	}


	private static ApplicationContext applicationContext;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		this.applicationContext = applicationContext;
	}

	
}




package com.wbo.currencyExchange.result;

public class CodeMsg {
	private int code;
	private String msg;
	
	//通用异常
	public static CodeMsg SUCCESS = new CodeMsg(0, "success");
	public static CodeMsg SERVER_ERROR = new CodeMsg(-10100, "服务端异常");
	public static CodeMsg BIND_ERROR = new CodeMsg(-10101, "参数校验异常：%s");
	//中间件 102XX
	public static CodeMsg MQ_CONFIRM_ERROR = new CodeMsg(-10201, "消息队列确认异常：%s");
	public static CodeMsg MQ_RETURN_ERROR = new CodeMsg(-10202, "消息队列投递异常：%s");
	
	
	//登录模块 103XX
	public static CodeMsg UNLOGIN_ERROR = new CodeMsg(-10301, "未登录");
	public static CodeMsg LOGIN_ERROR = new CodeMsg(-10302, "登录名或密码错误");
	
	//订单模块 104XX
	public static CodeMsg ORDER_AMOUNT_ERROR = new CodeMsg(-10401, "缺少订单数量");
	public static CodeMsg ORDER_PRICE_ERROR = new CodeMsg(-10402, "缺少订单价格");
	public static CodeMsg ORDER_TYPE_ERROR = new CodeMsg(-10403, "缺少订单类型");
	
	
	private CodeMsg(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public int getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}
	
	public CodeMsg fillArgs(Object...args) {
		int code = this.code;
		String msg = String.format(this.msg, args);
		return new CodeMsg(code, msg);
	}
}

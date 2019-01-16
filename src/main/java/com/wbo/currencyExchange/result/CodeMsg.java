package com.wbo.currencyExchange.result;

public class CodeMsg {
	private int code;
	private String msg;
	
	//通用异常
	public static CodeMsg SUCCESS = new CodeMsg(0, "success");
	public static CodeMsg SERVER_ERROR = new CodeMsg(10100, "服务端异常");
	public static CodeMsg BIND_ERROR = new CodeMsg(10101, "参数校验异常：%s");
	//登录模块 102XX
	public static CodeMsg LOGIN_ERROR = new CodeMsg(10201, "登录名或密码错误");
	
	
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

package com.wbo.currencyExchange.result;

public class ResultCode<T> {
	private int code;
	private String msg;
	private T data;

	/**
	 * 成功时候的调用
	 * */
	public static <T> ResultCode<T> success(T data){
		return new  ResultCode<T>(data);
	}
	
	/**
	 * 失败时候的调用
	 * */
	public static <T> ResultCode<T> error(CodeMsg cm){
		return new  ResultCode<T>(cm);
	}
	
	private ResultCode(T data) {
		this.code = 0;
		this.msg = "success";
		this.data = data;
	}
	
	private ResultCode(CodeMsg cm) {
		if(cm == null) {
			return;
		}
		this.code = cm.getCode();
		this.msg = cm.getMsg();
	}

	public int getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}
	public T getData() {
		return data;
	}
}

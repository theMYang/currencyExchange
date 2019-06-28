package com.wbo.currencyExchange.exception;

import com.wbo.currencyExchange.result.CodeMsg;

public class GlobalException extends RuntimeException{

	private CodeMsg cMsg;
	
	public GlobalException(CodeMsg cm) {
		super(cm.toString());
		this.cMsg = cm;
	}

	public CodeMsg getcMsg() {
		return cMsg;
	}
	
}

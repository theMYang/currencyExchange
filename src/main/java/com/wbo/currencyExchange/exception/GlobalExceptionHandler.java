package com.wbo.currencyExchange.exception;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wbo.currencyExchange.result.CodeMsg;
import com.wbo.currencyExchange.result.ResultCode;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

	@ExceptionHandler(value=Exception.class)
	public ResultCode<String> ExceptionHandler(HttpServletRequest request, Exception e){
		if(e instanceof GlobalException) {
			GlobalException exception = (GlobalException)e;
			return ResultCode.error(exception.getcMsg());
		}else if (e instanceof BindException) {
			BindException ex = (BindException)e;
			List<ObjectError> errors = ex.getAllErrors();
			ObjectError error = errors.get(0);
			String msg = error.getDefaultMessage();
			return ResultCode.error(CodeMsg.BIND_ERROR.fillArgs(msg));
		}else {
			return ResultCode.error(CodeMsg.SERVER_ERROR);
		}
	}
}

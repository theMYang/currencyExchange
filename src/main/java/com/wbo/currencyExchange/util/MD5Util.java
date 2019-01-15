package com.wbo.currencyExchange.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class MD5Util {

	private static Logger logger = LogManager.getLogger(MD5Util.class);
	
	private static final String SALT = "javaCAFE";
	
	public static String md5(String src) {
		return DigestUtils.md5Hex(src);
	}
	
	public static String EncoderByMd5(String passwd) {
		String str = SALT.charAt(1) + SALT.charAt(2) + SALT.charAt(4) + passwd + SALT.charAt(5) + SALT.charAt(7);
		return md5(str);
	}
	
}
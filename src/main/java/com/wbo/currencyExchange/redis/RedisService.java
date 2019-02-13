package com.wbo.currencyExchange.redis;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisService<K, V> {

	@Autowired
	private RedisTemplate<K, V> redisTemplate;
	
	//不设过期时间
	public static final int NOT_EXPIRE = -1;
	
	@SuppressWarnings("unchecked")
	public void setString(KeyPrefix keyPrefix, K key, V value) {
		int expireSeconds = keyPrefix.getExpireSeconds();
		String realKey = (String) (keyPrefix.getPrefix() + key);
		if(expireSeconds<0) {
			redisTemplate.opsForValue().set( (K) realKey, value);
		}else {
			redisTemplate.opsForValue().set( (K) realKey, value, expireSeconds, TimeUnit.SECONDS);
		}
	}
	
	public boolean setIfAbsentString(KeyPrefix keyPrefix, K key, V value) {
		String realKey = (String) (keyPrefix.getPrefix() + key);
		return redisTemplate.opsForValue().setIfAbsent( (K) realKey, value);
	}
	
	public <T> T getString(KeyPrefix keyPrefix, K key, Class<T> clazz) {
		String realKey = (String) (keyPrefix.getPrefix() + key);
		return (T) redisTemplate.opsForValue().get(realKey);
	}
	
	public synchronized boolean incrByBigDecimal(KeyPrefix keyPrefix, K key, BigDecimal val) {
		RedisTemplate<K, BigDecimal> redisTemplateTemp = new RedisTemplate<>();
		BigDecimal oldVal = this.getString(keyPrefix, key, BigDecimal.class);
		if(oldVal==null) {
			return false;
		}
		BigDecimal newVal = oldVal.add(val);
		setString(keyPrefix, key, (V) newVal);
		return true;
	}
	
	public synchronized boolean subByBigDecimal(KeyPrefix keyPrefix, K key, BigDecimal val) {
		RedisTemplate<K, BigDecimal> redisTemplateTemp = new RedisTemplate<>();
		BigDecimal oldVal = this.getString(keyPrefix, key, BigDecimal.class);
		if(oldVal==null) {
			return false;
		}
		BigDecimal newVal = oldVal.subtract(val);
		setString(keyPrefix, key, (V) newVal);
		return true;
	}
	
}

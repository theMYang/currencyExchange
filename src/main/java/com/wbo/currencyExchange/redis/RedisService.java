package com.wbo.currencyExchange.redis;

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
	
	public <T> T getString(KeyPrefix keyPrefix, K key, Class<T> clazz) {
		String realKey = (String) (keyPrefix.getPrefix() + key);
		return (T) redisTemplate.opsForValue().get(realKey);
	}
	
}

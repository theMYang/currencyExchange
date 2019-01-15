package com.wbo.currencyExchange;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.wbo.currencyExchange.domain.UserLogin;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DigitalCurrencyRedisTest {

	private static Logger logger = LogManager.getLogger(DigitalCurrencyRedisTest.class);

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;
	  
	@Test
	public void testRedis() {
		UserLogin user = new UserLogin();
        user.setLoginName("测不准的杂耍");
        user.setMobilePhone("13096930786");
        user.setPassword("12shit34");
        user.setUserLoginId(1416463316);
        user.setUserState(1);
        redisTemplate.opsForValue().set(user.getUserLoginId(),user);
        UserLogin result = (UserLogin) redisTemplate.opsForValue().get(user.getUserLoginId());
		logger.info("retJSON "+result.toString());
	}
}

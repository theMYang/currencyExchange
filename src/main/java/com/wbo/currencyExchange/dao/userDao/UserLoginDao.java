package com.wbo.currencyExchange.dao.userDao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.wbo.currencyExchange.domain.UserLogin;

//@Repository
@Mapper
public interface UserLoginDao {
	
//	@Select("select login_name, password, user_state, mobile_phone from user_login where user_login_id= #{id}")
//	public UserLogin getById(@Param("id") int id);
	
	public UserLogin getUserLogin(@Param("mobilePhone") String mobilePhone, @Param("password") String passwdByMd5);
}

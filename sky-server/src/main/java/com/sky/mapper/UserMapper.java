package com.sky.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.sky.entity.User;

/**
 * 用户登录mapper接口
 * @author tyq
 * @date 2026年9月4日
 * @project_name sky-server
 * @package_name com.sky.mapper
 * @file_name UserMapper.java
 * @classname UserMapper
 * @version 2026年9月4日 下午4:29:29
 */
@Mapper
public interface UserMapper {

	/**
	 * 根据openid查询用户
	 * @param openId
	 * @return
	 */
	@Select("select * from \"USER\" where openid = #{openId}")
	User queryByOpenId(String openId);

	/**
	 * 注册用户
	 * @param user
	 */
	@Insert("insert into \"USER\"(id,openid, name, phone, sex, id_number, avatar, create_time) " +
	        "values(SEQ_USER.nextval,#{openid}, #{name}, #{phone}, #{sex}, #{idNumber}, #{avatar}, #{createTime})")
	void register(User user);

}

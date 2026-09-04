package com.sky.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.UserNotLoginException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;

/**
 * 用户微信登录实现类
 * @author tyq
 * @date 2026年9月4日
 * @project_name sky-server
 * @package_name com.sky.service.impl
 * @file_name UserServiceImp.java
 * @classname UserServiceImp
 * @version 2026年9月4日 下午4:28:11
 */
@Service
public class UserServiceImp implements UserService{
	@Autowired
    private UserMapper userMapper;
	
	@Autowired
    private WeChatProperties weChatProperties;

	//微信请求url
	private static final String WX_URL = "https://api.weixin.qq.com/sns/jscode2session";
	
	/**
	 * 微信登录
	 */
	@Override
	public User wxLogin(UserLoginDTO userLoginDTO) {
		// 请求参数
		Map<String, String> map = new HashMap<>();
		map.put("appid", weChatProperties.getAppid());
		map.put("secret", weChatProperties.getSecret());
		map.put("js_code", userLoginDTO.getCode());
		map.put("grant_type", "authorization_code");
		//向微信发送请求携带参数
		String json = HttpClientUtil.doGet(WX_URL, map);
		//json格式转换为String
		JSONObject jsonObject = JSONObject.parseObject(json);
		//获取openid
		String openId = jsonObject.getString("openid");
		System.out.println("微信返回：" + json);
		if (openId == null) {
			throw new UserNotLoginException(MessageConstant.LOGIN_FAILED);
		}
		//判断是否存在openid,不存在注册
		User user = userMapper.queryByOpenId(openId);
		if (user == null) {
			user = User.builder().openid(openId).createTime(LocalDateTime.now()).build();
			userMapper.register(user);
		}
		return user;
	}
	
	
}

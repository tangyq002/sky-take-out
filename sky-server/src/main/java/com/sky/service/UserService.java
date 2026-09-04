package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * 用户登录
 * @author tyq
 * @date 2026年9月4日
 * @project_name sky-server
 * @package_name com.sky.service
 * @file_name UserService.java
 * @classname UserService
 * @version 2026年9月4日 下午4:27:50
 */
public interface UserService {

	/**
	 * 微信登录
	 * @param userLoginDTO
	 * @return
	 */
	User wxLogin(UserLoginDTO userLoginDTO);

}

package com.sky.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sky.result.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 店铺营业状态操作
 * @author tyq
 * @date 2026年9月3日
 * @project_name sky-server
 * @package_name com.sky.controller.admin
 * @file_name ShopController.java
 * @classname ShopController
 * @version 2026年9月3日 下午5:48:28
 */
@RestController("ShopUserController")
@RequestMapping("/user/shop")
@Slf4j
@Api(tags = "店铺营业状态操作相关接口")
public class ShopController {
	//店铺状态 redis的key常量
	public static final String KEY = "SHOP_STATUS";
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	/**
	 * 从redis获取营业状态
	 * @return
	 */
	@GetMapping("/status")
	@ApiOperation("获取营业状态")
	public Result<Integer> getStatus() {
		//获取营业状态
		Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
		if (status != null) {
			log.info("店铺营业状态为：{}",status == 1 ? "营业中" : "已打烊");
		}
		return Result.success(status);
	}
}

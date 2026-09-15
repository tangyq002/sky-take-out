package com.sky.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 购物车操作
 * @author tyq
 * @date 2026年9月14日
 * @project_name sky-server
 * @package_name com.sky.controller.user
 * @file_name ShoppingCartController.java
 * @classname ShoppingCartController
 * @version 2026年9月14日 上午12:58:07
 */
@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api(tags = "购物车相关接口")
public class ShoppingCartController {

	@Autowired
	private ShoppingCartService shoppingCartService;
	
	/**
	 * 添加购物车
	 * @param shoppingCartDTO
	 * @return
	 */
	@PostMapping("/add")
	@ApiOperation("添加购物车")
	public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
		log.info("添加购物车：{}",shoppingCartDTO);
		//加入购物车
		shoppingCartService.addCart(shoppingCartDTO);
		return Result.success();
	}

	/**
	 * 删除购物车中一个商品
	 * @param shoppingCartDTO
	 * @return
	 */
	@PostMapping("/sub")
	@ApiOperation("删除购物车中一个商品")
	public Result deleteById(@RequestBody ShoppingCartDTO shoppingCartDTO) {
		//删除
		shoppingCartService.deleteById(shoppingCartDTO);
		return Result.success();
	}
	
	/**
	 * 清空购物车
	 * @return
	 */
	@DeleteMapping("/clean")
	@ApiOperation("清空购物车")
	public Result clear() {
		//清空购物车
		shoppingCartService.delete();
		return Result.success();
	}
	
	/**
	 * 查看购物车
	 * @return
	 */
	@GetMapping("/list")
	@ApiOperation("查看购物车")
	public Result<List<ShoppingCart>> list() {
		//查看购物车
		List<ShoppingCart> list = shoppingCartService.list();
		return Result.success(list);
	}
}

package com.sky.service;

import java.util.List;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

/**
 * 购物车逻辑层
 * @author tyq
 * @date 2026年9月14日
 * @project_name sky-server
 * @package_name com.sky.service
 * @file_name ShoppingCartService.java
 * @classname ShoppingCartService
 * @version 2026年9月14日 上午1:03:58
 */
public interface ShoppingCartService {

	/**
	 * 添加购物车
	 * @param shoppingCartDTO
	 */
	void addCart(ShoppingCartDTO shoppingCartDTO);

	/**
	 * 查看购物车
	 * @return
	 */
	List<ShoppingCart> list();

	/**
	 * 清空购物车
	 */
	void delete();

	/**
	 * 删除单个商品根据id
	 * @param shoppingCartDTO
	 */
	void deleteById(ShoppingCartDTO shoppingCartDTO);

}

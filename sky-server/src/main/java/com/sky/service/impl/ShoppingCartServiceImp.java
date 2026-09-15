package com.sky.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;

/**
 * 购物车逻辑实现
 * @author tyq
 * @date 2026年9月14日
 * @project_name sky-server
 * @package_name com.sky.service.impl
 * @file_name ShoppingCartServiceImp.java
 * @classname ShoppingCartServiceImp
 * @version 2026年9月14日 上午1:04:25
 */
@Service
public class ShoppingCartServiceImp implements ShoppingCartService{

	@Autowired
    private ShoppingCartMapper shoppingCartMapper;
	@Autowired
	private DishMapper dishMapper;
	@Autowired
    private SetmealMapper setmealMapper;
	
	/**
	 * 添加购物车
	 */
	@Override
	public void addCart(ShoppingCartDTO shoppingCartDTO) {
		//获取当前用户id
		Long userId = BaseContext.getCurrentId();
		//获取当前用户购物车信息
		ShoppingCart shoppingCart = new ShoppingCart();
		//把DTO对象里的属性复制到实体类对象
		BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
		shoppingCart.setUserId(userId);
		List<ShoppingCart> shoppingCartList = shoppingCartMapper.queryShoppingCartByUserId(shoppingCart);
		//判断当前菜品是否存在于购物车 不为空存在
		if (shoppingCartList != null && shoppingCartList.size() > 0) {
			for (ShoppingCart cart : shoppingCartList) {
				shoppingCart = shoppingCartList.get(0);
				//存在 购物车商品数量加1
				shoppingCart.setNumber(shoppingCart.getNumber() + 1);
				shoppingCartMapper.update(shoppingCart);
			}
		}else {
			//不存在新增
			if (shoppingCartDTO.getDishId() != null) {
				//菜品加入购物车
				Dish dish = dishMapper.queryById(shoppingCartDTO.getDishId());
				shoppingCart.setName(dish.getName());
				shoppingCart.setImage(dish.getImage());
				shoppingCart.setAmount(dish.getPrice());
			}else {
				//套餐加入购物车
				Setmeal setmeal = setmealMapper.queryById(shoppingCartDTO.getSetmealId());
				shoppingCart.setName(setmeal.getName());
				shoppingCart.setImage(setmeal.getImage());
				shoppingCart.setAmount(setmeal.getPrice());
			}
			shoppingCart.setNumber(1);
			shoppingCart.setCreateTime(LocalDateTime.now());
			shoppingCartMapper.insert(shoppingCart);
		}
	}

	/**
	 * 根据当前用户id查看购物车
	 */
	@Override
	public List<ShoppingCart> list() {
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setUserId(BaseContext.getCurrentId());
		List<ShoppingCart> shoppingCartList =  shoppingCartMapper.queryShoppingCartByUserId(shoppingCart);
		return shoppingCartList;
	}

	/**
	 * 清空当前用户的购物车
	 */
	@Override
	public void delete() {
		shoppingCartMapper.delete(BaseContext.getCurrentId());
	}

	/**
	 * 根据id删除单个商品
	 */
	@Override
	public void deleteById(ShoppingCartDTO shoppingCartDTO) {
		//获取当前用户购物车信息
		ShoppingCart shoppingCart = new ShoppingCart();
		//把DTO对象里的属性复制到实体类对象
		BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
		shoppingCart.setUserId(BaseContext.getCurrentId());
		//查询购物车内容
		List<ShoppingCart> shoppingCartList = shoppingCartMapper.queryShoppingCartByUserId(shoppingCart);
		if (shoppingCartList != null && shoppingCartList.size() > 0) {
			//数量>1只数量-1
			if (shoppingCartList.get(0).getNumber() > 1 ) {
				shoppingCart = shoppingCartList.get(0);
				shoppingCart.setNumber(shoppingCartList.get(0).getNumber() - 1);
				shoppingCartMapper.update(shoppingCart);
			}else {
				//删除单个商品
				shoppingCart = shoppingCartList.get(0);
				shoppingCartMapper.deleteByCartId(shoppingCartList.get(0).getId());
			}
		}else {
			throw new DeletionNotAllowedException(MessageConstant.SHOPPING_CART_IS_NULL_NOT_DELETE);
		}
	}
}

package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.sky.entity.ShoppingCart;

/**
 * 操作购物车表mapper接口
 * @author tyq
 * @date 2026年9月14日
 * @project_name sky-server
 * @package_name com.sky.mapper
 * @file_name ShoppingCartMapper.java
 * @classname ShoppingCartMapper
 * @version 2026年9月14日 上午1:05:20
 */
@Mapper
public interface ShoppingCartMapper {

	/**
	 * 根据用户id查询购物车信息
	 * 根据菜品/套餐id查询菜品/套餐内容
	 * @param shoppingCart
	 * @return
	 */
	List<ShoppingCart> queryShoppingCartByUserId(ShoppingCart shoppingCart);

	/**
	 * 更新购物车
	 * @param shoppingCart
	 */
	void update(ShoppingCart shoppingCart);

	/**
	 * 添加购物车
	 * @param shoppingCart
	 */
	@Insert("insert into shopping_cart(id, name, image, user_id, dish_id, setmeal_id, dish_flavor, \"number\", amount, create_time) " +
	        "values (seq_shopping_cart.nextval, #{name}, #{image}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount}, #{createTime})")
	void insert(ShoppingCart shoppingCart);

	/**
	 * 清空购物车
	 * @param currentId
	 */
	@Delete("delete from shopping_cart where user_id = #{currentId}")
	void delete(Long currentId);

	/**
	 * 删除单条购物车商品
	 * @param id 购物车id
	 */
	@Delete("delete from shopping_cart where id = #{id}")
	void deleteByCartId(Long id);

}

package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.sky.entity.SetmealDish;

/**
 * 套餐菜品关系表
 * @author tyq
 * @date 2026年8月26日
 * @project_name sky-server
 * @package_name com.sky.mapper
 * @file_name SetmealDishMapper.java
 * @classname SetmealDishMapper
 * @version 2026年8月26日 下午7:27:54
 */
@Mapper
public interface SetmealDishMapper {

	/**
	 * 根据菜品id查询关联套餐
	 * @param ids
	 * @return
	 */
	List<Long> getByDishIds(List<Long> ids);

	/**
	 * 单个菜品查询关系
	 * @param id
	 * @return
	 */
	@Select("select setmeal_id from setmeal_dish where dish_id = #{id}")
	List<Long> getSetmealIdsByDishId(Long id);

	/**
	 * 新增菜品关系
	 * @param setmealDishes
	 */
	@Insert("insert into setmeal_dish(id,setmeal_id,dish_id,name,price,copies) " +
	        "values(seq_setmeal_dish.nextval,#{setmealId},#{dishId},#{name},#{price},#{copies})")
	void addSetmealDish(SetmealDish setmealDish);

	/**
	 * 根据套餐id删除菜品关系
	 * @param id
	 */
	@Delete("delete from setmeal_dish where setmeal_id = #{id}")
	void deleteBySetmealId(Long id);

	/**
	 * 根据套餐id查询关系表
	 * @param id
	 * @return
	 */
	@Select("select * from setmeal_dish where setmeal_id=#{id}")
	List<SetmealDish> queryBySetmealId(Long id);

}

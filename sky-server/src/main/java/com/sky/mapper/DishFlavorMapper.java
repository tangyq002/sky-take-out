package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.sky.entity.DishFlavor;

/**
 * 菜品口味表
 * @author tyq
 * @date 2026年8月26日
 * @project_name sky-server
 * @package_name com.sky.mapper
 * @file_name DishFlavorMapper.java
 * @classname DishFlavorMapper
 * @version 2026年8月26日 下午7:13:45
 */
@Mapper
public interface DishFlavorMapper {

	/**
	 * 新增菜品口味
	 * @param flavors
	 */
	@Insert("insert into dish_flavor(id,dish_id,name,value) " +
	        "values(seq_dish_flavor.nextval,#{dishId},#{name},#{value})")
	void addDishFlavor(DishFlavor dishFlavor);

	/**
	 * 根据id删除口味
	 * @param id
	 */
	@Delete("delete from dish_flavor where dish_id = #{id}")
	void deleteByDishId(Long id);

	/**
	 * 查询口味
	 * @param id
	 * @return
	 */
	@Select("select * from dish_flavor where dish_id = #{id}")
	List<DishFlavor> queryByDishId(Long id);

}

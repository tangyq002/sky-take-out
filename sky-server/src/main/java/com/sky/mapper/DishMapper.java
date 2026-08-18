package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 菜品mapper接口
 * @author tyq
 * @date 2026年8月18日
 * @project_name sky-server
 * @package_name com.sky.mapper
 * @file_name DishMapper.java
 * @classname DishMapper
 * @version 2026年8月18日 下午8:17:06
 */
@Mapper
public interface DishMapper {

	/**
	 * 查询菜品数量
	 * @param id
	 * @return
	 */
	@Select("select count(*) from dish where category_id=#{categoryId}")
	Integer countCategoryId(Long id);

}

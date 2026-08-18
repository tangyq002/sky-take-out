package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 套餐mapper接口
 * @author tyq
 * @date 2026年8月18日
 * @project_name sky-server
 * @package_name com.sky.mapper
 * @file_name setmealMapper.java
 * @classname setmealMapper
 * @version 2026年8月18日 下午8:24:15
 */
@Mapper
public interface SetmealMapper {
	/**
	 * 查询套餐数量
	 * @param id
	 * @return
	 */
	@Select("select count(*) from setmeal where category_id=#{categoryId}")
	Integer countCategoryId(Long id);

}

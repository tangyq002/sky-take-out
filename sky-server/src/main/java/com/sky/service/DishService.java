package com.sky.service;

import java.util.List;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

/**
 * 菜品管理接口
 * @author tyq
 * @date 2026年8月26日
 * @project_name sky-server
 * @package_name com.sky.service
 * @file_name DishService.java
 * @classname DishService
 * @version 2026年8月26日 下午6:44:01
 */
public interface DishService {

	PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

	void addDish(DishDTO dishDTO);

	void updateDish(DishDTO dishDTO);

	void deleteDishByIds(List<Long> ids);

	DishVO queryDishById(Long id);

	List<Dish> list(Long categoryId);

	void updateStatus(Integer status, Long id);

}

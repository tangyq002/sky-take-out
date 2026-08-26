package com.sky.service;

import java.util.List;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

/**
 * 套餐业务层
 * @author tyq
 * @date 2026年8月24日
 * @project_name sky-server
 * @package_name com.sky.service
 * @file_name SetmealService.java
 * @classname SetmealService
 * @version 2026年8月24日 下午11:25:35
 */
public interface SetmealService {

	PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

	void addSetmeal(SetmealDTO setmealDTO);

	void updateSetmeal(SetmealDTO setmealDTO);

	SetmealVO querySetmealById(Long id);

	void deleteSetmealByIds(List<Long> ids);

	void updateStatus(Integer status, Long id);

}

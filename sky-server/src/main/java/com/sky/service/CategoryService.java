package com.sky.service;

import java.util.List;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

/**
 * 分类管理service
 * @author tyq
 * @date 2026年8月18日
 * @project_name sky-server
 * @package_name com.sky.service
 * @file_name CategoryService.java
 * @classname CategoryService
 * @version 2026年8月18日 下午4:41:26
 */
public interface CategoryService {

	/**
	 * 分页查询
	 * @param categoryPageQueryDTO
	 * @return
	 */
	PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

	/**
	 * 新增分类
	 * @param categoryDTO
	 */
	void addCategory(CategoryDTO categoryDTO);

	void updateCategory(CategoryDTO categoryDTO);

	List<Category> list(Integer type);

	void deleteCategoryById(Long id);

	void updateStatus(Integer status, Long id);

}

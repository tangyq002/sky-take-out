package com.sky.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 分类管理
 * @author tyq
 * @date 2026年8月18日
 * @project_name sky-server
 * @package_name com.sky.controller.admin
 * @file_name CategoryController.java
 * @classname CategoryController
 * @version 2026年8月18日 下午4:39:01
 */
@RestController("AdminCategoryController")
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "分类管理相关接口")
public class CategoryController {
	@Autowired
    private CategoryService categoryService;
	
	/**
	 * 分类分页查询
	 * @param categoryPageQueryDTO
	 * @return
	 */
	@GetMapping("/page")
	@ApiOperation("分类分页查询")
	public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO){
		//输出到日志
		log.info("分类分页查询的实体类信息：{}",categoryPageQueryDTO);
		//分页查询
		PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
		return Result.success(pageResult);
		
	}
	
	/**
	 * 新增分类
	 * @param categoryDTO
	 * @return
	 */
	@PostMapping
	@ApiOperation("新增分类")
	public Result add(@RequestBody CategoryDTO categoryDTO) {
		log.info("新增分类内容：{}",categoryDTO);
		//新增分类
		categoryService.addCategory(categoryDTO);
		return Result.success();
	}

	/**
	 * 修改分类
	 * @param categoryDTO
	 * @return
	 */
	@PutMapping
	@ApiOperation("修改分类")
	public Result update(@RequestBody CategoryDTO categoryDTO) {
		//修改分类
		categoryService.updateCategory(categoryDTO);
		return Result.success();
	}
	
	/**
	 * 根据id删除分类
	 * @param id
	 * @return
	 */
	@DeleteMapping
	@ApiOperation("删除分类")
	public Result delete(Long id) {
		//删除分类
		categoryService.deleteCategoryById(id);
		return Result.success();
	}
	
	/**
	 * 根据类型查询分类
	 * @param type
	 * @return
	 */
	@GetMapping("/list")
	@ApiOperation("根据类型查询分类")
	public Result<List<Category>> list(Integer type) {
		//根据类型查询分类
		List<Category> list = categoryService.list(type);
		return Result.success(list);
	}
	
	/**
	 *  启用禁用分类
	 * @param status
	 * @param id
	 * @return
	 */
	@PostMapping("/status/{status}")
	@ApiOperation("启用禁用分类")
	public Result updateStatus(@PathVariable("status")Integer status,Long id) {
		//修改状态
		categoryService.updateStatus(status,id);
		return Result.success();
	}
}

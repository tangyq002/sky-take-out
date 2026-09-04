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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 套餐管理
 * @author tyq
 * @date 2026年8月24日
 * @project_name sky-server
 * @package_name com.sky.controller.admin
 * @file_name SetmealController.java
 * @classname SetmealController
 * @version 2026年8月24日 下午11:14:29
 */
@RestController("AdminSetmealController")
@RequestMapping("/admin/setmeal")
@Slf4j
public class SetmealController {
	@Autowired
    private SetmealService setmealService;
	
	/**
	 * 套餐分页查询
	 * @param setmealPageQueryDTO
	 * @return
	 */
	@GetMapping("/page")
	@ApiOperation("套餐分页查询")
	public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
		//输出到日志
		log.info("套餐分页查询的实体类信息：{}",setmealPageQueryDTO);
		//分页查询
		PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
		return Result.success(pageResult);
	}
	
	/**
	 * 新增套餐
	 * @param setmealDTO
	 * @return
	 */
	@PostMapping
	@ApiOperation("新增套餐")
	public Result add(@RequestBody SetmealDTO setmealDTO) {
		log.info("新增套餐内容：{}",setmealDTO);
		//新增套餐
		setmealService.addSetmeal(setmealDTO);
		return Result.success();
	}

	/**
	 * 修改套餐
	 * @param setmealDTO
	 * @return
	 */
	@PutMapping
	@ApiOperation("修改套餐")
	public Result update(@RequestBody SetmealDTO setmealDTO) {
		//修改套餐
		setmealService.updateSetmeal(setmealDTO);
		return Result.success();
	}
	
	/**
	 * 批量删除套餐
	 * @param ids
	 * @return
	 */
	@DeleteMapping
	@ApiOperation("批量删除套餐")
	public Result delete(@RequestParam List<Long> ids) {
		//批量删除
		setmealService.deleteSetmealByIds(ids);
		return Result.success();
	}
	
	/**
	 * 根据id查询套餐
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	@ApiOperation("根据id查询套餐")
	public Result<SetmealVO> queryById(@PathVariable("id") Long id) {
		//根据id查询套餐
		SetmealVO setmealVO = setmealService.querySetmealById(id);
		return Result.success(setmealVO);
	}
	
	/**
	 * 套餐起售、停售
	 * @param status
	 * @param id
	 * @return
	 */
	@PostMapping("/status/{status}")
	@ApiOperation("套餐起售、停售")
	public Result updateStatus(@PathVariable("status")Integer status,Long id) {
		//修改状态
		setmealService.updateStatus(status,id);
		return Result.success();
	}
}

package com.sky.controller.admin;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 菜品管理
 * @author tyq
 * @date 2026年8月26日
 * @project_name sky-server
 * @package_name com.sky.controller.admin
 * @file_name DishController.java
 * @classname DishController
 * @version 2026年8月26日 下午6:42:04
 */
@RestController("AdminDishController")
@RequestMapping("/admin/dish")
@Slf4j
public class DishController {
	@Autowired
    private DishService dishService;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	/**
	 * 菜品分页查询
	 * @param dishPageQueryDTO
	 * @return
	 */
	@GetMapping("/page")
	@ApiOperation("菜品分页查询")
	public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
		//输出到日志
		log.info("菜品分页查询的实体类信息：{}",dishPageQueryDTO);
		//分页查询
		PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
		return Result.success(pageResult);
	}
	
	/**
	 * 新增菜品
	 * @param dishDTO
	 * @return
	 */
	@PostMapping
	@ApiOperation("新增菜品")
	public Result add(@RequestBody DishDTO dishDTO) {
		log.info("新增菜品内容：{}",dishDTO);
		//新增菜品
		dishService.addDish(dishDTO);
		String key = "dish_" + dishDTO.getCategoryId();
		clearRedis(key);
		return Result.success();
	}

	/**
	 * 修改菜品
	 * @param dishDTO
	 * @return
	 */
	@PutMapping
	@ApiOperation("修改菜品")
	public Result update(@RequestBody DishDTO dishDTO) {
		//修改菜品
		dishService.updateDish(dishDTO);
		clearRedis("dish_*");
		return Result.success();
	}
	
	/**
	 * 批量删除菜品
	 * @param ids
	 * @return
	 */
	@DeleteMapping
	@ApiOperation("批量删除菜品")
	public Result delete(@RequestParam List<Long> ids) {
		log.info("批量删除的菜品：{}", ids);
		//批量删除
		dishService.deleteDishByIds(ids);
		clearRedis("dish_*");
		return Result.success();
	}
	
	/**
	 * 根据id查询菜品
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	@ApiOperation("根据id查询菜品")
	public Result<DishVO> queryById(@PathVariable("id") Long id) {
		//根据id查询菜品
		DishVO dishVO = dishService.queryDishById(id);
		return Result.success(dishVO);
	}
	
	/**
	 * 根据分类id查询菜品
	 * @param categoryId
	 * @return
	 */
	@GetMapping("/list")
	@ApiOperation("根据分类id查询菜品")
	public Result<List<Dish>> list(Long categoryId) {
		//根据分类id查询菜品
		List<Dish> list = dishService.list(categoryId);
		return Result.success(list);
	}
	
	/**
	 * 菜品起售、停售
	 * @param status
	 * @param id
	 * @return
	 */
	@PostMapping("/status/{status}")
	@ApiOperation("菜品起售、停售")
	public Result updateStatus(@PathVariable("status")Integer status,Long id) {
		//修改状态
		dishService.updateStatus(status,id);
		clearRedis("dish_*");
		return Result.success();
	}
	
	/**
	 * 根据条件清除redis缓存
	 * @param key
	 */
	public void clearRedis(String key) {
		Set keys = redisTemplate.keys(key);
		redisTemplate.delete(keys);
	}
}

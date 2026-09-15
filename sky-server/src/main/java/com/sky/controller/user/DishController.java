package com.sky.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * c端菜品浏览接口
 * @author tyq
 * @date 2026年9月4日
 * @project_name sky-server
 * @package_name com.sky.controller.user
 * @file_name DishController.java
 * @classname DishController
 * @version 2026年9月4日 下午9:12:46
 */
@RestController("UserDishController")
@RequestMapping("/user/dish")
@Api(tags = "c端菜品浏览接口")
@Slf4j
public class DishController {
	
	@Autowired
    private DishService dishService;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {
    	//设置菜品唯一key
    	String key = "dish_" + categoryId;
    	//获取缓存中的数据
    	List<DishVO> dishVOList = (List<DishVO>) redisTemplate.opsForValue().get(key);
    	//判断缓存中是否存在该数据，存在返回数据
    	if (dishVOList != null && !dishVOList.isEmpty()) {
			return Result.success(dishVOList);
		}
    	//缓存中不存在，从数据库查出保存到redis缓存
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        //起售中的菜品
        dish.setStatus(StatusConstant.ENABLE);
        //查询菜品
        List<DishVO> list = dishService.queryAll(dish);
        //保存到缓存
        redisTemplate.opsForValue().set(key, list);
        return Result.success(list);
    }
}

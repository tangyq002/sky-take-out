package com.sky.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	
    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        //起售中的菜品
        dish.setStatus(StatusConstant.ENABLE);
        //查询菜品
        List<DishVO> list = dishService.queryAll(dish);
        return Result.success(list);
    }
}

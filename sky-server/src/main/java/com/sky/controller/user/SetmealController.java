package com.sky.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sky.constant.StatusConstant;
import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * C端-套餐浏览接口
 * @author tyq
 * @date 2026年9月4日
 * @project_name sky-server
 * @package_name com.sky.controller.user
 * @file_name SetmealController.java
 * @classname SetmealController
 * @version 2026年9月4日 下午9:42:52
 */
@RestController("UserSetmealController")
@RequestMapping("/user/setmeal")
@Api(tags = "套餐浏览接口")
@Slf4j
public class SetmealController {
	@Autowired
    private SetmealService setmealService;
	
    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Setmeal>> list(Long categoryId) {
    	Setmeal setmeal = new Setmeal();
    	setmeal.setCategoryId(categoryId);
        //起售中的套餐
    	setmeal.setStatus(StatusConstant.ENABLE);
        //查询套餐
        List<Setmeal> list = setmealService.list(setmeal);
        return Result.success(list);
    }
    
    /**
     * 根据套餐id查询包含的菜品
     * @param id
     * @return
     */
    @GetMapping("/dish/{id}")
    @ApiOperation("根据套餐id查询包含的菜品")
    public Result<List<DishItemVO>> queryDishesById(@PathVariable("id") Long id) {
    	//查询菜品
        List<DishItemVO> list = setmealService.queryDishesById(id);
        return Result.success(list);
    }
}

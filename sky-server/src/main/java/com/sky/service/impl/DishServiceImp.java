package com.sky.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;

/**
 * 菜品管理业务层
 * @author tyq
 * @date 2026年8月26日
 * @project_name sky-server
 * @package_name com.sky.service.impl
 * @file_name DishServiceImp.java
 * @classname DishServiceImp
 * @version 2026年8月26日 下午6:57:43
 */
@Service
public class DishServiceImp implements DishService{
	@Autowired
    private DishMapper dishMapper;
	@Autowired
	private DishFlavorMapper dishFlavorMapper;
	@Autowired
    private SetmealDishMapper setmealDishMapper;
	@Autowired
    private SetmealMapper setmealMapper;
	
	/**
	 * 菜品分页查询
	 */
	@Override
	public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
		//当前页和显示数
		PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
		//分页查询
		Page<Category> page = dishMapper.pageQuery(dishPageQueryDTO);
		//返回当前页码数据
		return new PageResult(page.getTotal(),page.getResult());
	}

	/**
	 * 新增菜品，菜品表和菜品口味表
	 */
	@Override
	public void addDish(DishDTO dishDTO) {
		Dish dish = new Dish();
		//把DTO对象里的属性复制到实体类对象
		BeanUtils.copyProperties(dishDTO, dish);
		//设置状态 默认停售
		dish.setStatus(StatusConstant.DISABLE);
		//新增菜品
		dishMapper.addDish(dish);
	    //获取口味集合
	    List<DishFlavor> flavors = dishDTO.getFlavors();
	    //判断口味是否为空
	    if (flavors != null && flavors.size() > 0) {
	        //设置菜品id
	        for (DishFlavor flavor : flavors) {
	            flavor.setDishId(dish.getId());
	            //插入口味表
		        dishFlavorMapper.addDishFlavor(flavor);
	        }
	    }
	}

	/**
	 * 修改菜品
	 */
	@Override
	public void updateDish(DishDTO dishDTO) {
	    Dish dish = new Dish();
	    BeanUtils.copyProperties(dishDTO, dish);
	    //修改菜品
	    dishMapper.updateDish(dish);
	    //删除旧口味
	    dishFlavorMapper.deleteByDishId(dish.getId());
	    //获取新口味
	    List<DishFlavor> flavors = dishDTO.getFlavors();
	    if (flavors != null && flavors.size() > 0) {
	        for (DishFlavor flavor : flavors) {
	            flavor.setDishId(dish.getId());
	            //插入口味表
		        dishFlavorMapper.addDishFlavor(flavor);
	        }
	    }
	}

	/**
	 * 批量删除菜品
	 * 在售状态下 被套餐关联的菜品  不能删除
	 */
	@Override
	public void deleteDishByIds(List<Long> ids) {
		//判断当前状态
		for (Long id : ids) {
			Dish dish = dishMapper.queryById(id);
			if (StatusConstant.ENABLE == dish.getStatus()) {
		        throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
		    }
		}
		//查询关联的套餐
		List<Long> setmealIds = setmealDishMapper.getByDishIds(ids);
	    	if (setmealIds != null && setmealIds.size() > 0) {
	            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
	        }
		//循环删除
		for (Long id : ids) {
			//删除菜品口味表
		    dishFlavorMapper.deleteByDishId(id);
		    //删除菜品
			dishMapper.deleteById(id);
		}
	}

	/**
	 * 根据id查询菜品
	 * 查询菜品已经菜品口味
	 */
	@Override
	public DishVO queryDishById(Long id) {
		//查询菜品
		Dish dish = dishMapper.queryById(id);
		//查询口味
		List<DishFlavor> flavors = dishFlavorMapper.queryByDishId(id);
		//封装到vo
		DishVO dishVO = new DishVO();
		BeanUtils.copyProperties(dish, dishVO);
		dishVO.setFlavors(flavors);
		return dishVO;
	}

	/**
	 * 根据分类id查询菜品
	 */
	@Override
	public List<Dish> list(Long categoryId) {
		return dishMapper.list(categoryId);
	}

	/**
	 * 菜品起售停售
	 * 菜品停售，包含菜品的套餐同时停售
	 */
	@Override
	public void updateStatus(Integer status, Long id) {
		//菜品停售，包含菜品的套餐同时停售
		if (status == StatusConstant.DISABLE) {
	        //查询包含该菜品的套餐id
	        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishId(id);
	        //判断是否有关联套餐
	        if (setmealIds != null && setmealIds.size() > 0) {
	            //修改套餐状态
	            for (Long setmealId : setmealIds) {
	                Setmeal setmeal = new Setmeal();
	                setmeal.setId(setmealId);
	                setmeal.setStatus(StatusConstant.DISABLE);
	                // 修改套餐状态
	                setmealMapper.updateStatus(setmeal);
	            }
	        }
		}
		//修改菜品状态
		Dish dish = new Dish();
	    //设置修改信息
		dish.setId(id);
	    //设置状态
		dish.setStatus(status);

		dishMapper.updateStatus(dish);
	}

}

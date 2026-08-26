package com.sky.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;

/**
 * 套餐管理实现类
 * @author tyq
 * @date 2026年8月24日
 * @project_name sky-server
 * @package_name com.sky.service.impl
 * @file_name SetmealServiceImp.java
 * @classname SetmealServiceImp
 * @version 2026年8月24日 下午11:38:42
 */
@Service
public class SetmealServiceImp implements SetmealService{
	
	@Autowired
    private SetmealMapper setmealMapper;
	@Autowired
    private DishMapper dishMapper;
	@Autowired
    private SetmealDishMapper setmealDishMapper;

	/**
	 * 套餐管理分页查询
	 */
	@Override
	public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
		//当前页和显示数
		PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
		//分页查询
		Page<Setmeal> page = setmealMapper.pageQuery(setmealPageQueryDTO);
		//返回当前页码数据
		return new PageResult(page.getTotal(),page.getResult());
	}

	/**
	 * 新增套餐
	 */
	@Override
	public void addSetmeal(SetmealDTO setmealDTO) {
		Setmeal setmeal = new Setmeal();
		//把DTO对象里的属性复制到实体类对象
		BeanUtils.copyProperties(setmealDTO, setmeal);
		//新增套餐默认为停售，状态为0
		setmeal.setStatus(StatusConstant.DISABLE);
		//新增套餐
		setmealMapper.addSetmeal(setmeal);
	    //插入套餐和菜品关系
	    List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
	    if (setmealDishes != null && setmealDishes.size() > 0) {
	        for (SetmealDish setmealDish : setmealDishes) {
	            setmealDish.setSetmealId(setmeal.getId());
	            //保存新的关联关系
	            setmealDishMapper.addSetmealDish(setmealDish);
	        }
	    }
	}

	/**
	 * 修改套餐
	 */
	@Override
	public void updateSetmeal(SetmealDTO setmealDTO) {
		Setmeal setmeal = new Setmeal();
		//把DTO对象里的属性复制到实体类对象
		BeanUtils.copyProperties(setmealDTO, setmeal);
		//修改套餐表
		setmealMapper.updateSetmeal(setmeal);
	    //删除原关联表
	    setmealDishMapper.deleteBySetmealId(setmeal.getId());
	    //获取新的菜品
	    List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
	    if (setmealDishes != null && setmealDishes.size() > 0) {
	        for (SetmealDish setmealDish : setmealDishes) {
	            setmealDish.setSetmealId(setmeal.getId());
	            //保存新的关联关系
	            setmealDishMapper.addSetmealDish(setmealDish);
	        }
	    }
	}

	/**
	 * 根据id查询套餐
	 */
	@Override
	public SetmealVO querySetmealById(Long id) {
	    //查询套餐
	    Setmeal setmeal = setmealMapper.queryById(id);
	    //查询关系表
	    List<SetmealDish> setmealDishes = setmealDishMapper.queryBySetmealId(id);
	    //封装到vo
	    SetmealVO setmealVO = new SetmealVO();
	    BeanUtils.copyProperties(setmeal, setmealVO);
	    setmealVO.setSetmealDishes(setmealDishes);
	    return setmealVO;
	}

	/**
	 * 批量删除套餐
	 */
	@Override
	public void deleteSetmealByIds(List<Long> ids) {
		//起售状态下，不可删除套餐
		//判断当前状态
		for (Long id : ids) {
			Setmeal setmeal = setmealMapper.queryById(id);
			if (StatusConstant.ENABLE == setmeal.getStatus()) {
		        throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
		    }
		}
		//循环删除
		for (Long id : ids) {
			//删除套餐菜品关系
	        setmealDishMapper.deleteBySetmealId(id);
		    //删除套餐
		    setmealMapper.deleteById(id);
		}
	}

	/**
	 * 套餐起售停售
	 */
	@Override
	public void updateStatus(Integer status, Long id) {
		//套餐内如果有停售菜品，则套餐无法起售卖
		//当起售时
		if (status == StatusConstant.ENABLE) {
			//查询套餐下的菜品
			List<Dish> dishList = dishMapper.getBySetmealId(id);
		    //判断是否有停售
		    for (Dish dish : dishList) {
		        if (dish.getStatus() == StatusConstant.DISABLE) {
		            throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
		        }
		    }
		}
		//修改状态
		Setmeal setmeal = new Setmeal();
	    //设置修改信息
		setmeal.setId(id);
	    //设置状态
		setmeal.setStatus(status);
		setmealMapper.updateStatus(setmeal);
	}

}

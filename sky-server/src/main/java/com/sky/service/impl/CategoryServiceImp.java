package com.sky.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;

/**
 * service实现类
 * @author tyq
 * @date 2026年8月18日
 * @project_name sky-server
 * @package_name com.sky.service.impl
 * @file_name CategoryServiceImp.java
 * @classname CategoryServiceImp
 * @version 2026年8月18日 下午4:42:50
 */
@Service
public class CategoryServiceImp implements CategoryService{
	@Autowired
    private CategoryMapper categoryMapper;
	@Autowired
    private DishMapper dishMapper;
	@Autowired
    private SetmealMapper setmealMapper;

	/**
	 * 分类管理分页查询
	 */
	@Override
	public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
		//当前页和显示数
		PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());
		//分页查询
		Page<Category> page = categoryMapper.pageQuery(categoryPageQueryDTO);
		//返回当前页码数据
		return new PageResult(page.getTotal(),page.getResult());
	}

	/**
	 * 新增分类
	 */
	@Override
	public void addCategory(CategoryDTO categoryDTO) {
		Category category = new Category();
		//把DTO对象里的属性复制到实体类对象
		BeanUtils.copyProperties(categoryDTO, category);
		//设置状态 创建/修改时间 创建人/修改人id
		category.setStatus(StatusConstant.ENABLE);
//		//当前时间
//		category.setCreateTime(LocalDateTime.now());
//		category.setUpdateTime(LocalDateTime.now());
//		//当前用户id
//		category.setCreateUser(BaseContext.getCurrentId()); 
//		category.setUpdateUser(BaseContext.getCurrentId());
		//新增
		categoryMapper.addCategory(category);
	}

	/**
	 * 修改分类
	 */
	@Override
	public void updateCategory(CategoryDTO categoryDTO) {
		Category category = new Category();
		//把DTO对象里的属性复制到实体类对象
		BeanUtils.copyProperties(categoryDTO, category);
		//设置修改时间 修改人id
		category.setStatus(StatusConstant.ENABLE);
//		//当前时间
//		category.setUpdateTime(LocalDateTime.now());
//		//当前用户id
//		category.setUpdateUser(BaseContext.getCurrentId());
		//修改
		categoryMapper.updateCategory(category);
	}

	/**
	 * 根据类型查询分类
	 */
	@Override
	public List<Category> list(Integer type) {
		return categoryMapper.list(type);
	}

	/**
	 * 根据id删除分类
	 */
	@Override
	public void deleteCategoryById(Long id) {
		//查询当前分类下是否有菜品
		Integer count = dishMapper.countCategoryId(id);
	    //如果有产品提示 ”分类下有产品不可删除”
	    if(count > 0){
	        throw new DeletionNotAllowedException("分类下有菜品，不能删除");
	    }
        //查询当前分类是否有套餐
	    Integer count2 = setmealMapper.countCategoryId(id);
        if(count2 > 0){
            //当前分类下有套餐，不能删除
            throw new DeletionNotAllowedException("分类下有套餐，不能删除");
        }
        //删除
        categoryMapper.deleteCategoryById(id);
	}

	/**
	 * 启用禁用分类
	 */
	@Override
	public void updateStatus(Integer status, Long id) {
	    Category category = new Category();
	    //设置修改信息
	    category.setId(id);
	    //设置状态
	    category.setStatus(status);
	    category.setUpdateTime(LocalDateTime.now());
	    category.setUpdateUser(BaseContext.getCurrentId());
	    categoryMapper.updateStatus(category);
	}
}

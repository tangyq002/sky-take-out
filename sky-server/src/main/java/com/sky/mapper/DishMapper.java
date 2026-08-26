package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;

/**
 * 菜品mapper接口
 * @author tyq
 * @date 2026年8月18日
 * @project_name sky-server
 * @package_name com.sky.mapper
 * @file_name DishMapper.java
 * @classname DishMapper
 * @version 2026年8月18日 下午8:17:06
 */
@Mapper
public interface DishMapper {

	/**
	 * 查询菜品数量
	 * @param id
	 * @return
	 */
	@Select("select count(*) from dish where category_id=#{categoryId}")
	Integer countCategoryId(Long id);

	/**
	 * 根据id查询套餐菜品
	 * @param id
	 * @return
	 */
	@Select("select d.* from dish d join setmeal_dish sd on d.id = sd.dish_id where sd.setmeal_id = #{id}")
	List<Dish> getBySetmealId(Long id);

	/**
	 * 分页查询菜品
	 * @param dishPageQueryDTO
	 * @return
	 */
	Page<Category> pageQuery(DishPageQueryDTO dishPageQueryDTO);

	/**
	 * 新增菜品
	 * @param dish
	 */
	@AutoFill(OperationType.INSERT)
	@SelectKey(statement = "select seq_dish.nextval from dual",keyProperty = "id",before = true,resultType = Long.class)
	@Insert("insert into dish(id,name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) " +
	        "values(#{id},#{name}, #{categoryId}, #{price}, #{image}, #{description}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
	void addDish(Dish dish);

	/**
	 * 修改菜品
	 * @param dish
	 */
	@AutoFill(OperationType.UPDATE)
	void updateDish(Dish dish);

	/**
	 * 根据id查询菜品
	 * @param id
	 * @return
	 */
	@Select("select * from dish where id = #{id}")
	Dish queryById(Long id);

	/**
	 * 根据id删除菜品
	 * @param id
	 */
	@Delete("delete from dish where id = #{id}")
	void deleteById(Long id);

	/**
	 * 根据分类id查询菜品
	 * @param categoryId
	 * @return
	 */
	@Select("select * from dish where category_id = #{categoryId}")
	List<Dish> list(Long categoryId);

	/**
	 * 更新状态
	 * @param dish
	 */
	@Update("update dish set status = #{status}, update_time = #{updateTime}, update_user = #{updateUser} where id = #{id}")
	@AutoFill(OperationType.UPDATE)
	void updateStatus(Dish dish);

}

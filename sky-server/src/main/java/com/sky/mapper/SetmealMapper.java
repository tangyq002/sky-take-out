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
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;

/**
 * 套餐mapper接口
 * @author tyq
 * @date 2026年8月18日
 * @project_name sky-server
 * @package_name com.sky.mapper
 * @file_name setmealMapper.java
 * @classname setmealMapper
 * @version 2026年8月18日 下午8:24:15
 */
@Mapper
public interface SetmealMapper {
	/**
	 * 查询套餐数量
	 * @param id
	 * @return
	 */
	@Select("select count(*) from setmeal where category_id=#{categoryId}")
	Integer countCategoryId(Long id);

	/**
	 * 套餐分页查询
	 * @param setmealPageQueryDTO
	 * @return
	 */
	Page<Setmeal> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

	/**
	 * 新增套餐
	 * @param setmeal
	 */
	@Insert("insert into setmeal(id,name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) " +
	        "values(#{id},#{name}, #{categoryId}, #{price}, #{image}, #{description}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
	@SelectKey(statement = "select seq_setmeal.nextval from dual",keyProperty = "id",before = true,resultType = Long.class)
	@AutoFill(OperationType.INSERT)
	void addSetmeal(Setmeal setmeal);

	/**
	 * 修改套餐
	 * @param setmeal
	 */
	@AutoFill(OperationType.UPDATE)
	void updateSetmeal(Setmeal setmeal);

	/**
	 * 根据id查询套餐
	 * @param id
	 * @return
	 */
	@Select("select * from setmeal where id = #{id}")
	Setmeal queryById(Long id);

	/**
	 * 根据id删除套餐
	 * @param id
	 */
	@Delete("delete from setmeal where id = #{id}")
	void deleteById(Long id);

	/**
	 * 更新状态
	 * @param setmeal
	 */
	@Update("update setmeal set status = #{status}, update_time = #{updateTime}, update_user = #{updateUser} where id = #{id}")
	@AutoFill(OperationType.UPDATE)
	void updateStatus(Setmeal setmeal);

	/**
	 * 条件查询套餐列表
	 * @param setmeal
	 * @return
	 */
	List<Setmeal> list(Setmeal setmeal);

	/**
	 * 用户端查询套餐包含的菜品
	 * @param id
	 * @return
	 */
    @Select("select sd.name, sd.copies, d.image, d.description from setmeal_dish sd join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{id}")
	List<DishItemVO> queryDishesById(Long id);

}

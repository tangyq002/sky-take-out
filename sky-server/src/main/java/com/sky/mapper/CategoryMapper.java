package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;

/**
 * 分类管理mapper接口
 * @author tyq
 * @date 2026年8月18日
 * @project_name sky-server
 * @package_name com.sky.mapper
 * @file_name CategoryMapper.java
 * @classname CategoryMapper
 * @version 2026年8月18日 下午4:44:17
 */
@Mapper
public interface CategoryMapper {

	/**
	 * 分类分页查询
	 * @param categoryPageQueryDTO
	 * @return
	 */
	Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

	/**
	 * 新增分类
	 * @param category
	 */
	@Insert("insert into category (id, type, name, sort, status, create_time, update_time, create_user, update_user) "
			+ "values (seq_category.nextval, #{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
	@AutoFill(OperationType.INSERT)
	void addCategory(Category category);

	/**
	 * 修改分类
	 * @param category
	 */
	@AutoFill(OperationType.UPDATE)
	void updateCategory(Category category);

	/**
	 * 根据类型查分类
	 * @param type
	 * @return
	 */
	@Select("select * from category where type=#{type} order by sort")
	List<Category> list(Integer type);

	/**
	 * 根据id删除分类
	 * @param id
	 */
	@Delete("delete from category where id=#{id}")
	void deleteCategoryById(Long id);

	/**
	 * 启用禁用分类
	 * @param category
	 */
	@Update("update category set status=#{status}, update_time=#{updateTime}, update_user=#{updateUser} where id=#{id}")
	void updateStatus(Category category);

}

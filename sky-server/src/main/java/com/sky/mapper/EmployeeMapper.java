package com.sky.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 分页查询员工
     * @param employeePageQueryDTO
     * @return
     */
	Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

	/**
	 * 新增员工
	 * @param employee
	 */
	@Insert("insert into employee (id, username, name, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user) " +
			"values(seq_employee.nextval, #{username}, #{name}, #{password}, #{phone}, #{sex}, #{idNumber}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
	void addEmployee(Employee employee);

	/**
	 * 根据id查询员工
	 * @param id
	 * @return
	 */
	@Select("select * from employee where id = #{id}")
	Employee queryById(Long id);

	/**
	 * 修改员工
	 * @param employee
	 */
	void updateEmployee(Employee employee);

	/**
	 * 启用禁用员工
	 * @param employee
	 */
	@Update("update employee set status=#{status}, update_time=#{updateTime}, update_user=#{updateUser} where id=#{id}")
	void updateStatus(Employee employee);

}

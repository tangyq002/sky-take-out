package com.sky.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @Override
	public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 员工管理分页查询
     */
	@Override
	public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
		//当前页和显示数
		PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
		//分页查询
		Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);
		//返回当前页码数据
		return new PageResult(page.getTotal(),page.getResult());
	}

	/**
	 * 添加员工
	 */
	@Override
	public void addEmployee(EmployeeDTO employeeDTO) {
		Employee employee = new Employee();
		//把DTO对象里的属性复制到实体类对象
		BeanUtils.copyProperties(employeeDTO, employee);
		//设置状态 默认密码 创建/修改时间 创建人/修改人id
		employee.setPassword(PasswordConstant.DEFAULT_PASSWORD);
		employee.setStatus(StatusConstant.ENABLE);
		//当前时间
		employee.setCreateTime(LocalDateTime.now());
		employee.setUpdateTime(LocalDateTime.now());
		//当前用户id
		employee.setCreateUser(BaseContext.getCurrentId());
		employee.setUpdateUser(BaseContext.getCurrentId());
		//新增
		employeeMapper.addEmployee(employee);
	}

	/**
	 * 根据id查询员工
	 */
	@Override
	public Employee queryEmployeeById(Long id) {
		return employeeMapper.queryById(id);
	}

	/**
	 * 修改员工
	 */
	@Override
	public void updateEmployee(EmployeeDTO employeeDTO) {
		Employee employee = new Employee();
		//把DTO对象里的属性复制到实体类对象
		BeanUtils.copyProperties(employeeDTO, employee);
		//设置 修改时间 修改人id
		employee.setUpdateTime(LocalDateTime.now());
		employee.setUpdateUser(BaseContext.getCurrentId());
		//修改
		employeeMapper.updateEmployee(employee);
	}

	/**
	 * 启用、禁用员工账号
	 */
	@Override
	public void updateStatus(Integer status, Long id) {
		Employee employee = new Employee();
	    //要修改员工的id
		employee.setId(id);
	    //设置状态
		employee.setStatus(status);
		employee.setUpdateTime(LocalDateTime.now());
		employee.setUpdateUser(BaseContext.getCurrentId());
		employeeMapper.updateStatus(employee);
	}

}

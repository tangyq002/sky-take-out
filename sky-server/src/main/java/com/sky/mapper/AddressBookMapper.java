package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.sky.entity.AddressBook;

/**
 * 地址簿mapper接口
 * @author tyq
 * @date 2026年9月15日
 * @project_name sky-server
 * @package_name com.sky.mapper
 * @file_name AddressBookMapper.java
 * @classname AddressBookMapper
 * @version 2026年9月15日 下午4:52:19
 */
@Mapper
public interface AddressBookMapper {

	/**
	 * 新增地址
	 * @param addressBook
	 */
	@Insert("insert into address_book(id,user_id,consignee,sex,phone,province_code,province_name,city_code,city_name,district_code,district_name,detail,label,is_default)"
	        + " values(SEQ_ADDRESS_BOOK.nextval,#{userId},#{consignee},#{sex},#{phone},#{provinceCode},#{provinceName},#{cityCode},#{cityName},#{districtCode},#{districtName},#{detail},#{label},#{isDefault})")
	void add(AddressBook addressBook);

	/**
	 * 获取当前用户的所有地址列表
	 * @param addressBook
	 * @return
	 */
	List<AddressBook> queryAll(AddressBook addressBook);

	/**
	 * 修改地址根据id
	 * @param addressBook
	 */
	void update(AddressBook addressBook);

	/**
	 * 根据id删除地址
	 * @param id
	 */
	@Delete("delete from address_book where id = #{id}")
	void deleteById(Long id);

	/**
	 * 根据id查询地址
	 * @param id
	 * @return
	 */
	@Select("select * from address_book where id = #{id}")
	AddressBook queryById(Long id);

	/**
	 * 根据用户id修改地址
	 * @param addressBook
	 */
	void updateByUserId(AddressBook addressBook);

}

package com.sky.service;

import java.util.List;

import com.sky.entity.AddressBook;

/**
 * 地址簿业务层
 * @author tyq
 * @date 2026年9月15日
 * @project_name sky-server
 * @package_name com.sky.service
 * @file_name AddressBookService.java
 * @classname AddressBookService
 * @version 2026年9月15日 下午4:51:01
 */
public interface AddressBookService {

	/**
	 * 新增地址
	 * @param addressBook
	 */
	void addAddressBook(AddressBook addressBook);

	/**
	 * 查询当前登录用户的所有地址信息
	 * @param addressBook
	 * @return
	 */
	List<AddressBook> list(AddressBook addressBook);

	/**
	 * 根据id修改地址
	 * @param addressBook
	 */
	void updateAddress(AddressBook addressBook);

	/**
	 * 根据id删除地址
	 * @param id
	 */
	void deleteAddressById(Long id);

	/**
	 * 根据id查询地址
	 * @param id
	 * @return
	 */
	AddressBook queryAddressById(Long id);

	/**
	 * 设置默认地址
	 * @param addressBook
	 */
	void setDefaultAddress(AddressBook addressBook);

}

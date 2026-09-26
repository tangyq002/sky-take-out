package com.sky.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;

/**
 * 地址簿业务实现类
 * @author tyq
 * @date 2026年9月15日
 * @project_name sky-server
 * @package_name com.sky.service.impl
 * @file_name AddressBookServiceImp.java
 * @classname AddressBookServiceImp
 * @version 2026年9月15日 下午4:51:28
 */
@Service
public class AddressBookServiceImp implements AddressBookService{

	@Autowired
    private  AddressBookMapper addressBookMapper;
	
	/**
	 * 新增地址
	 */
	@Override
	public void addAddressBook(AddressBook addressBook) {
		//获取当前用户id
        addressBook.setUserId(BaseContext.getCurrentId());
        //设置为非默认地址
        addressBook.setIsDefault(StatusConstant.DISABLE);
        
        //查询用户地址簿列表
        List<AddressBook> list = addressBookMapper.queryAll(addressBook);
        //如果为第一次新增地址，设置为默认地址
        if (list.size() == 0 || list.isEmpty()) {
        	addressBook.setIsDefault(StatusConstant.ENABLE);
        	addressBookMapper.update(addressBook);
		}
        addressBookMapper.add(addressBook);
	}

	/**
	 * 获取当前用户的所有地址列表
	 */
	@Override
	public List<AddressBook> list(AddressBook addressBook) {
        return addressBookMapper.queryAll(addressBook);
	}

	/**
	 * 根据id修改地址
	 */
	@Override
	public void updateAddress(AddressBook addressBook) {
		 addressBookMapper.update(addressBook);
	}

	/**
	 * 根据id删除
	 */
	@Override
	public void deleteAddressById(Long id) {
		addressBookMapper.deleteById(id);
	}

	/**
	 * 根据id查询地址
	 */
	@Override
	public AddressBook queryAddressById(Long id) {
	    return addressBookMapper.queryById(id);
	}
	
	/**
	 * 设置默认地址
	 */
	@Override
	public void setDefaultAddress(AddressBook addressBook) {
		//把其他地址改为非默认 0
		addressBook.setIsDefault(0);
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookMapper.updateByUserId(addressBook);
		//当前地址改为默认 1
        addressBook.setIsDefault(1);
        addressBookMapper.update(addressBook);
	}

}

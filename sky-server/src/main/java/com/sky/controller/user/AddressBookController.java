package com.sky.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 地址簿相关接口
 * @author tyq
 * @date 2026年9月14日
 * @project_name sky-server
 * @package_name com.sky.controller.user
 * @file_name AddressBookController.java
 * @classname AddressBookController
 * @version 2026年9月14日 下午9:28:20
 */
@RestController
@RequestMapping("/user/addressBook")
@Slf4j
@Api(tags = "地址簿相关接口")
public class AddressBookController {
	
	@Autowired
    private AddressBookService addressBookService;
	
	/**
	 * 新增地址
	 * @param addressBook
	 * @return
	 */
	@PostMapping
	@ApiOperation("新增地址")
	public Result add(@RequestBody AddressBook addressBook) {
		log.info("新增地址内容：{}",addressBook);
		//新增地址
		addressBookService.addAddressBook(addressBook);
		return Result.success();
	}
	
	/**
	 * 查询当前登录用户的所有地址信息
	 * @return
	 */
	@GetMapping("/list")
	@ApiOperation("查询当前登录用户的所有地址信息")
	public Result<List<AddressBook>> list(){
        AddressBook addressBook = new AddressBook();
        //根据当前用户id查
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> list = addressBookService.list(addressBook);
        return Result.success(list);
	}
	
	/**
	 * 查询当前登录用户的所有地址信息
	 * @return
	 */
	@GetMapping("/default")
	@ApiOperation("查询默认地址")
	public Result<AddressBook> queryDefaultAddress(){
        AddressBook addressBook = new AddressBook();
        //默认值为1的
        addressBook.setIsDefault(1);
        addressBook.setUserId(BaseContext.getCurrentId());
        //查出全部
        List<AddressBook> list = addressBookService.list(addressBook);
        if (list != null && list.size() == 1) {
            return Result.success(list.get(0));
        }
        return Result.error("未查询到默认地址，请先设置");
	}

	/**
	 * 根据id修改地址
	 * @param addressBook
	 * @return
	 */
	@PutMapping
	@ApiOperation("根据id修改地址")
	public Result update(@RequestBody AddressBook addressBook) {
		//修改地址
		addressBookService.updateAddress(addressBook);
		return Result.success();
	}
	
	/**
	 * 根据id删除地址
	 * @param id
	 * @return
	 */
	@DeleteMapping
	@ApiOperation("根据id删除地址")
	public Result delete(@RequestParam Long id) {
		log.info("删除的地址：{}", id);
		//根据id删除
		addressBookService.deleteAddressById(id);
		return Result.success();
	}
	
	/**
	 * 根据id查询地址
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	@ApiOperation("根据id查询地址")
	public Result<AddressBook> queryById(@PathVariable("id") Long id) {
		//根据id查询地址
		AddressBook addressBook = addressBookService.queryAddressById(id);
		return Result.success(addressBook);
	}
	
	/**
	 * 设置默认地址
	 * @param addressBook
	 * @return
	 */
	@PutMapping("/default")
	@ApiOperation("设置默认地址")
	public Result set(@RequestBody AddressBook addressBook) {
		//设置设置默认地址
        addressBookService.setDefaultAddress(addressBook);
        return Result.success();
	}

}

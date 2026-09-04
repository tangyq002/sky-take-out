package com.sky.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
/**
 * 用户端分类接口
 * @author tyq
 * @date 2026年9月4日
 * @project_name sky-server
 * @package_name com.sky.controller.user
 * @file_name CategoryController.java
 * @classname CategoryController
 * @version 2026年9月4日 下午6:46:25
 */
@RestController("UserCategoryController")
@RequestMapping("/user/category")
@Api(tags = "用户端分类接口")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 查询分类
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查询分类")
    public Result<List<Category>> list(Integer type) {
    	//查询分类
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }
}

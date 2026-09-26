package com.sky.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 管理端订单相关
 * @author tyq
 * @date 2026年9月25日
 * @project_name sky-server
 * @package_name com.sky.controller.user
 * @file_name OrderController.java
 * @classname OrderController
 * @version 2026年9月25日 下午10:33:08
 */
@RestController("AdminOrderController")
@RequestMapping("/admin/order")
@Api(tags = "管理端订单相关接口")
@Slf4j
public class OrderController {

	@Autowired
    private OrderService orderService;
	
	/**
	 * 订单搜索
	 * @param ordersPageQueryDTO
	 * @return
	 */
	@GetMapping("/conditionSearch")
	@ApiOperation("订单搜索")
	public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
		PageResult pageResult = orderService.conditionSearch(ordersPageQueryDTO);
		return Result.success(pageResult);
	}
	
	/**
	 * 完成订单
	 * @param id 订单id
	 * @return
	 */
	@PutMapping("/complete/{id}")
	@ApiOperation("完成订单")
	public Result complete(@PathVariable Long id) {
		orderService.complete(id);
		return Result.success();
	}
	
	/**
	 * 拒单
	 * @param ordersRejectionDTO
	 * @return
	 */
	@PutMapping("/rejection")
	@ApiOperation("拒单")
	public Result rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO) {
		orderService.rejection(ordersRejectionDTO);
		return Result.success();
	}
	
	/**
	 * 接单
	 * @param ordersConfirmDTO
	 * @return
	 */
	@PutMapping("/confirm")
	@ApiOperation("接单")
	public Result confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
		orderService.confirm(ordersConfirmDTO);
		return Result.success();
	}
	
	/**
	 * 查询订单详情
	 * @param id
	 * @return
	 */
	@GetMapping("/details/{id}")
	@ApiOperation("查询订单详情")
	public Result<OrderVO> queryOrderDetail(@PathVariable Long id) {
		OrderVO orderVO = orderService.queryOrderDetail(id);
		return Result.success(orderVO);
	}
	
	/**
	 * 各个状态的订单数量统计
	 * @return
	 */
	@GetMapping("/statistics")
	@ApiOperation("各个状态的订单数量统计")
	public Result<OrderStatisticsVO> statistics() {
		OrderStatisticsVO orderStatisticsVO= orderService.statistics();
		return Result.success(orderStatisticsVO);
	}
	
	/**
	 * 取消订单
	 * @param ordersCancelDTO
	 * @return
	 */
	@PutMapping("/cancel")
	@ApiOperation("取消订单")
	public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) {
		orderService.cancel(ordersCancelDTO);
		return Result.success();
	}
	
	/**
	 * 派送订单
	 * @param id
	 * @return
	 */
	@PutMapping("/delivery/{id}")
	@ApiOperation("派送订单")
	public Result delivery(@PathVariable Long id) {
		orderService.delivery(id);
		return Result.success();
	}
}

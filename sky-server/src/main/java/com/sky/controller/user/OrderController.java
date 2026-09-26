package com.sky.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户端订单相关
 * @author tyq
 * @date 2026年9月25日
 * @project_name sky-server
 * @package_name com.sky.controller.user
 * @file_name OrderController.java
 * @classname OrderController
 * @version 2026年9月25日 下午10:33:08
 */
@RestController("UserOrderController")
@RequestMapping("/user/order")
@Api(tags = "c端订单相关接口")
@Slf4j
public class OrderController {

	@Autowired
    private OrderService orderService;
	
	/**
	 * 用户下单
	 * @param ordersSubmitDTO
	 * @return
	 */
	@PostMapping("/submit")
	@ApiOperation("用户下单")
	public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
		log.info("用户下单内容：{}",ordersSubmitDTO);
		//下单
		OrderSubmitVO orderSubmitVO = orderService.submit(ordersSubmitDTO);
		return Result.success(orderSubmitVO);
	}
	
	/**
	 * 订单支付
	 * @param ordersPaymentDTO
	 * @return
	 */
	@PutMapping("/payment")
	@ApiOperation("订单支付")
	public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) {
		log.info("微信支付：{}",ordersPaymentDTO);
		//支付
		OrderPaymentVO ordserPaymentVO = orderService.payment(ordersPaymentDTO);
		return Result.success(ordserPaymentVO);
	}
	
	/**
	 * 再来一单
	 * @param id
	 * @return
	 */
	@PostMapping("/repetition/{id}")
	@ApiOperation("再来一单")
	public Result repetition(@PathVariable Long id) {
		orderService.repetition(id);
		return Result.success();
	}
	
	/**
	 * 分页查询历史订单
	 * @param page 页面
	 * @param pageSize 页面记录数
	 * @param status 订单状态
	 * @return
	 */
	@GetMapping("/historyOrders")
	@ApiOperation("历史订单查询")
	public Result<PageResult> queryHistoryOrders(int page, int pageSize, Integer status) {
		PageResult pageResult = orderService.page(page, pageSize, status);
		return Result.success(pageResult);
	}
	
	/**
	 * 查询订单详情
	 * @param id
	 * @return
	 */
	@GetMapping("/orderDetail/{id}")
	@ApiOperation("查询订单详情")
	public Result<OrderVO> queryOrderDetail(@PathVariable Long id) {
		OrderVO orderVO = orderService.queryOrderDetail(id);
		return Result.success(orderVO);
	}
	
	/**
	 * 取消订单
	 * @param id
	 * @return
	 */
	@PutMapping("/cancel/{id}")
	@ApiOperation("取消订单")
	public Result cancel(@PathVariable Long id) {
		orderService.cancel(id);
		return Result.success();
	}
}

package com.sky.service;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

/**
 * 用户订单服务层
 * @author tyq
 * @date 2026年9月25日
 * @project_name sky-server
 * @package_name com.sky.service
 * @file_name OrderService.java
 * @classname OrderService
 * @version 2026年9月25日 下午10:38:25
 */
public interface OrderService {

	/**
	 * 用户下单
	 * @param ordersSubmitDTO
	 * @return
	 */
	OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);

	/**
	 * 用户支付
	 * @param ordersPaymentDTO
	 * @return
	 */
	OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO);

	/**
	 * 再来一单
	 * @param id
	 */
	void repetition(Long id);

	/**
	 * 分页查询历史订单
	 * @param page
	 * @param pageSize
	 * @param status
	 * @return
	 */
	PageResult page(int page, int pageSize, Integer status);

	/**
	 * 查询订单详情
	 * @param id
	 * @return
	 */
	OrderVO queryOrderDetail(Long id);

	/**
	 * 用户端取消订单
	 * @param id
	 */
	void cancel(Long id);
	
	/**
	 * 订单搜索
	 * @param ordersPageQueryDTO
	 * @return
	 */
	PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);
	
	/**
	 * 管理端取消订单
	 * @param ordersCancelDTO
	 */
	void cancel(OrdersCancelDTO ordersCancelDTO);

	/**
	 * 各个状态的订单数量统计
	 * @return
	 */
	OrderStatisticsVO statistics();

	/**
	 * 完成订单
	 * @param id
	 */
	void complete(Long id);

	/**
	 * 拒单
	 * @param ordersRejectionDTO
	 */
	void rejection(OrdersRejectionDTO ordersRejectionDTO);

	/**
	 * 接单
	 * @param ordersConfirmDTO
	 */
	void confirm(OrdersConfirmDTO ordersConfirmDTO);

	/**
	 * 派送订单
	 * @param id
	 */
	void delivery(Long id);

}

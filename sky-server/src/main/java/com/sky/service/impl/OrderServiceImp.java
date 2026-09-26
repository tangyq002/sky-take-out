package com.sky.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

/**
 * 用户订单服务层实现
 * @author tyq
 * @date 2026年9月25日
 * @project_name sky-server
 * @package_name com.sky.service.impl
 * @file_name OrderServiceImp.java
 * @classname OrderServiceImp
 * @version 2026年9月25日 下午10:38:59
 */
@Service
public class OrderServiceImp implements OrderService{

    @Autowired
    private OrderMapper orderMapper;
	@Autowired
    private  AddressBookMapper addressBookMapper;
	@Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;
	
	/**
	 * 用户下单
	 */
	@Override
	@Transactional
	public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {
		//判断用户的地址是否为空
		AddressBook addressBook  = addressBookMapper.queryById(ordersSubmitDTO.getAddressBookId());
		if (addressBook == null) {
			throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
		}
		//判断购物车是否为空,当前用户id查询该用户购物车列表
		ShoppingCart shoppingCart = new ShoppingCart();
		//当前用户id
		Long userId = BaseContext.getCurrentId();
		shoppingCart.setUserId(userId);
		List<ShoppingCart> shoppingCartList = shoppingCartMapper.queryShoppingCartByUserId(shoppingCart);
		if (shoppingCartList == null || shoppingCartList.isEmpty()) {
			throw new AddressBookBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
		}
		//插入订单表
		Orders orders = new Orders();
		//把DTO对象里的属性复制到实体类对象
		BeanUtils.copyProperties(ordersSubmitDTO, orders);
		orders.setOrderTime(LocalDateTime.now());
		//订单状态 待付款
		orders.setStatus(orders.PENDING_PAYMENT);
		//支付状态 未支付
		orders.setPayStatus(orders.UN_PAID);
		//订单号 当前时间
		orders.setNumber(String.valueOf(System.currentTimeMillis()));
		//插入用户id 手机号 收货人 地址
		orders.setUserId(userId);
		orders.setPhone(addressBook.getPhone());
		orders.setConsignee(addressBook.getConsignee());
		orders.setAddress(
		        addressBook.getProvinceName()
		        + addressBook.getCityName()
		        + addressBook.getDistrictName()
		        + addressBook.getDetail()
		);
		orderMapper.insert(orders);
		
		//插入订单明细表
		//循环插入，单条
		for (ShoppingCart cart : shoppingCartList) {
			OrderDetail orderDetail = new OrderDetail();
			BeanUtils.copyProperties(cart, orderDetail);
			//设置订单id
			orderDetail.setOrderId(orders.getId());
		    //单条插入订单明细
		    orderDetailMapper.insert(orderDetail);
		}
		//清空购物车
		shoppingCartMapper.delete(userId);
		//组装到vo返回
		OrderSubmitVO orderSubmitVO = new OrderSubmitVO();
		//设置下单时间 总金额 订单号 订单id
		orderSubmitVO.setOrderTime(orders.getOrderTime());
		orderSubmitVO.setOrderAmount(orders.getAmount());
		orderSubmitVO.setOrderNumber(orders.getNumber());
		orderSubmitVO.setId(orders.getId());
		return orderSubmitVO;
	}

	/**
	 * 用户支付
	 */
	@Override
	public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) {
		//支付成功后 更新订单状态为5待接单 支付状态为1已支付
		Orders orders = new Orders();
		orders.setStatus(Orders.TO_BE_CONFIRMED);
		orders.setPayStatus(Orders.PAID);
		//设置修改时间
		orders.setOrderTime(LocalDateTime.now());
		//根据订单号查询订单
		Orders orders2 = orderMapper.queryByNumber(ordersPaymentDTO.getOrderNumber());
		//订单不存在
		if (orders2 == null) {
			throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
		}
		//设置订单id
		orders.setId(orders2.getId());
		//更新订单
		orderMapper.update(orders);
		//返回vo，设为已付款
		OrderPaymentVO orderPaymentVO = new OrderPaymentVO();
		orderPaymentVO.setPackageStr(String.valueOf(Orders.PAID));
		return orderPaymentVO;
	}

	/**
	 * 再来一单 订单商品加入到购物车
	 */
	@Override
	public void repetition(Long id) {
		//获取当前用户id
		Long userId = BaseContext.getCurrentId();
	    //根据订单id查询订单明细
	    List<OrderDetail> orderDetailList = orderDetailMapper.queryByOrderId(id);
	    //循环将订单明细添加到购物车
	    for (OrderDetail orderDetail : orderDetailList) {
	        ShoppingCart shoppingCart = new ShoppingCart();
	        BeanUtils.copyProperties(orderDetail, shoppingCart, "id");
	        //设置当前用户id
	        shoppingCart.setUserId(userId);
	        //设置创建时间
	        shoppingCart.setCreateTime(LocalDateTime.now());
	        //单条插入
	        shoppingCartMapper.insert(shoppingCart);
	    }
	}

	/**
	 * 分页查询历史订单
	 */
	@Override
	public PageResult page(int page, int pageSize, Integer status) {
		//当前页和显示数
		PageHelper.startPage(page,pageSize);
	    //查询当前用户订单
	    Long userId = BaseContext.getCurrentId();
	    List<Orders> ordersList = orderMapper.pageQuery(userId, status);
	    Page<Orders> pageResult = (Page<Orders>) ordersList;
	    List<OrderVO> orderVOList = new ArrayList<OrderVO>();
	    //遍历订单
	    for (Orders orders : ordersList) {
		    //组装到vo
	        OrderVO orderVO = new OrderVO();
	        //复制订单信息
	        BeanUtils.copyProperties(orders, orderVO);
	        //查询明细
	        List<OrderDetail> orderDetailList = orderDetailMapper.queryByOrderId(orders.getId());
	        //设置订单明细
	        orderVO.setOrderDetailList(orderDetailList);
	        orderVOList.add(orderVO);
	    }
	    return new PageResult(pageResult.getTotal(),orderVOList);
	}

	/**
	 * 根据订单id查询订单详情
	 */
	@Override
	public OrderVO queryOrderDetail(Long id) {
		//查询订单表
		Orders orders = orderMapper.queryById(id);
		//查询订单明细
		List<OrderDetail> orderDetailList = orderDetailMapper.queryByOrderId(id);
		//组装到vo
		OrderVO orderVO = new OrderVO();
	    BeanUtils.copyProperties(orders, orderVO);
	    orderVO.setOrderDetailList(orderDetailList);
		return orderVO;
	}

	/**
	 * 取消订单
	 */
	@Override
	public void cancel(Long id) {
		//根据id查询订单
        Orders orders = orderMapper.queryById(id);
        //如果订单状态为 3已接单 4派送中 5已完成
        //提示订单状态错误
        if (orders.getStatus() > 2) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        //如果为2待接单状态下取消 可以退款
        if (orders.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            //支付状态改为退款
        	orders.setPayStatus(Orders.REFUND);
        }
        //更新订单
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason("用户取消");
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
	}

	/**
	 * 订单搜索分页查询 条件查询
	 */
	@Override
	public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
		//当前页和显示数
		PageHelper.startPage(ordersPageQueryDTO.getPage(),ordersPageQueryDTO.getPageSize());
		//分页查询
		Page<Orders> page = orderMapper.pageQuery2(ordersPageQueryDTO);
	    //查询订单列表
	    List<Orders> ordersList = page.getResult();
	    List<OrderVO> orderVOList = new ArrayList<OrderVO>();
	    //遍历订单
	    for (Orders orders : ordersList) {
		    //组装到vo
	        OrderVO orderVO = new OrderVO();
	        //复制订单信息
	        BeanUtils.copyProperties(orders, orderVO);
	        //查询明细
	        List<OrderDetail> orderDetailList = orderDetailMapper.queryByOrderId(orders.getId());
	        //设置订单明细
	        orderVO.setOrderDetailList(orderDetailList);
	        orderVOList.add(orderVO);
	    }
	    return new PageResult(page.getTotal(),orderVOList);
	}

	/**
	 * 管理端取消订单
	 */
	@Override
	public void cancel(OrdersCancelDTO ordersCancelDTO) {
		//根据id查询订单
        Orders orders = orderMapper.queryById(ordersCancelDTO.getId());
        //更新订单状态 取消原因 时间
        orders.setId(ordersCancelDTO.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason(ordersCancelDTO.getCancelReason());
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
	}

	/**
	 * 统计各个状态的订单数量
	 */
	@Override
	public OrderStatisticsVO statistics() {
        //查询 待接单 待派送 派送中的订单数量
        Integer toBeConfirmed = orderMapper.countStatus(Orders.TO_BE_CONFIRMED);
        Integer confirmed = orderMapper.countStatus(Orders.CONFIRMED);
        Integer deliveryInProgress = orderMapper.countStatus(Orders.DELIVERY_IN_PROGRESS);
        //封装到vo返回
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setToBeConfirmed(toBeConfirmed);
        orderStatisticsVO.setConfirmed(confirmed);
        orderStatisticsVO.setDeliveryInProgress(deliveryInProgress);
        return orderStatisticsVO;
	}

	/**
	 * 完成订单
	 */
	@Override
	public void complete(Long id) {
		//根据id查订单
		Orders orders = orderMapper.queryById(id);
		//如果状态为不为4已完成
        if (orders == null || !orders.getStatus().equals(Orders.DELIVERY_IN_PROGRESS)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        //更新订单状态为完成
        orders.setStatus(Orders.COMPLETED);
        orders.setDeliveryTime(LocalDateTime.now());
        orderMapper.update(orders);
	}

	/**
	 * 拒单
	 */
	@Override
	public void rejection(OrdersRejectionDTO ordersRejectionDTO) {
		//根据id查询订单
        Orders orders = orderMapper.queryById(ordersRejectionDTO.getId());
        //待接单2状态可以进行拒单
        if (orders == null || !orders.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        //更新订单为6已取消 拒单原因 时间
        orders.setStatus(Orders.CANCELLED);
        orders.setRejectionReason(ordersRejectionDTO.getRejectionReason());
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
		
	}

	/**
	 * 接单
	 */
	@Override
	public void confirm(OrdersConfirmDTO ordersConfirmDTO) {
		Orders orders = new Orders();
		orders.setId(ordersConfirmDTO.getId());
		//设置状态为3已接单
		orders.setStatus(Orders.CONFIRMED);
		orderMapper.update(orders);
	}

	/**
	 * 派送订单
	 */
	@Override
	public void delivery(Long id) {
        Orders orders = orderMapper.queryById(id);
        //状态为3待派送
        if (orders == null || !orders.getStatus().equals(Orders.CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        //状态变为4派送中
        orders.setStatus(Orders.DELIVERY_IN_PROGRESS);
        orderMapper.update(orders);
	}
}

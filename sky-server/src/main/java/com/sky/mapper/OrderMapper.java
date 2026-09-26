package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;

/**
 * 用户订单mapper层
 * @author tyq
 * @date 2026年9月25日
 * @project_name sky-server
 * @package_name com.sky.mapper
 * @file_name OrderMapper.java
 * @classname OrderMapper
 * @version 2026年9月25日 下午10:40:34
 */
@Mapper
public interface OrderMapper {

	/**
	 * 新增订单
	 * @param orders
	 */
	@SelectKey(statement = "select seq_orders.nextval from dual",keyProperty = "id",before = true,resultType = Long.class)
	@Insert("insert into orders(id,\"number\",status,user_id,address_book_id,order_time,checkout_time,pay_method,pay_status,amount,remark,phone,address,user_name,consignee,cancel_reason,rejection_reason,cancel_time,estimated_delivery_time,delivery_status,delivery_time,pack_amount,tableware_number,tableware_status) "
	        + "values(#{id},#{number},#{status},#{userId},#{addressBookId},#{orderTime},#{checkoutTime},#{payMethod},#{payStatus},#{amount},#{remark},#{phone},#{address},#{userName},#{consignee},#{cancelReason},#{rejectionReason},#{cancelTime},#{estimatedDeliveryTime},#{deliveryStatus},#{deliveryTime},#{packAmount},#{tablewareNumber},#{tablewareStatus})")
	void insert(Orders orders);

	/**
	 * 根据订单号查询订单
	 * @param orderNumber
	 * @return
	 */
	@Select("select * from orders where \"number\" = #{orderNumber}")
	Orders queryByNumber(String orderNumber);

	/**
	 * 更新订单
	 * @param orders
	 */
	void update(Orders orders);

	/**
	 * 分页查询历史订单
	 * @param userId
	 * @param status
	 * @return
	 */
	List<Orders> pageQuery(Long userId, Integer status);

	/**
	 * 根据订单id查订单
	 * @param id
	 * @return
	 */
	@Select("select * from orders where id = #{id}")
	Orders queryById(Long id);

	/**
	 * 订单条件查询
	 * @param ordersPageQueryDTO
	 * @return
	 */
	Page<Orders> pageQuery2(OrdersPageQueryDTO ordersPageQueryDTO);

	/**
	 * 统计各个状态的订单数量
	 * @param toBeConfirmed
	 * @return
	 */
	@Select("select count(*) from orders where status = #{status}")
	Integer countStatus(Integer status);

}

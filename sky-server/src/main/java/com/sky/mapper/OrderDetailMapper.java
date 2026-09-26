package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.sky.entity.OrderDetail;

/**
 * 订单详情数据层
 * @author tyq
 * @date 2026年9月25日
 * @project_name sky-server
 * @package_name com.sky.mapper
 * @file_name OrderDetailMapper.java
 * @classname OrderDetailMapper
 * @version 2026年9月25日 下午11:56:32
 */
@Mapper
public interface OrderDetailMapper {

	/**
	 * 插入订单明细
	 * @param orderDetail
	 */
	@Insert("insert into order_detail(id,name,image,order_id,dish_id,setmeal_id,dish_flavor,\"number\",amount) "
	        + "values(seq_order_detail.nextval,#{name},#{image},#{orderId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount})")
	void insert(OrderDetail orderDetail);

	/**
	 * 根据订单id查询订单明细
	 * @param id
	 * @return
	 */
	@Select("select * from order_detail where order_id = #{id}")
	List<OrderDetail> queryByOrderId(Long id);
	
}

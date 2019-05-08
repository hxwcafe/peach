package com.example.peach.dao;

import com.example.peach.pojo.OrderRefund;
import com.example.peach.pojo.merge.OrderRAct;

import java.util.List;

public interface OrderRefundMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderRefund record);

    int insertSelective(OrderRefund record);

    OrderRefund selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderRefund record);

    int updateByPrimaryKey(OrderRefund record);
    //根据用户得到acticityId
    OrderRAct selectorderacticityId(Integer userId);
    //根据用户得到orderpayId
    OrderRAct selectorderpayId(Integer userId);
    //根据用户查看退款的状态
    OrderRefund selectStatusByuseerId(Integer userId);
    //查询所有订单
    List<OrderRAct> selectOrderRAct();
    //查所有待处理得订单
    List<OrderRefund> selectallByStatus();
}
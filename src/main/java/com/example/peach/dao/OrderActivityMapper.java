package com.example.peach.dao;

import com.example.peach.pojo.OrderActivity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderActivityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderActivity record);

    int insertSelective(OrderActivity record);

    OrderActivity selectByPrimaryKey(Integer id);

    int updateByOutTradeNo(OrderActivity record);

    int updateByPrimaryKey(OrderActivity record);
    //根据用户id查询所有活动订单
    List<OrderActivity> selectOrderUA(Integer userId);
    //根据商户订单查询活动订单信息
    OrderActivity selectOrderUAByOut(String outTradeNo);
    //支付回调orderA and actOrder修改表
    int updatePayBack(@Param(value = "outTradeNo") String outTradeNo, @Param(value = "status") Integer status, @Param(value = "tradeStatus") String tradeStatus);
}
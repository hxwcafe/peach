package com.example.peach.service;

import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.OrderActivity;

public interface OrderActivityService {
    //插入活动订单信息
    ServiceResponse<String> insertOrderActivity(OrderActivity orderActivity);
    //根据商户订单查询商户信息
    ServiceResponse<OrderActivity> selectOrderActivity(String out);
    //根据商户订单修改商户信息
    ServiceResponse<String> updateByOutTradeNo(OrderActivity orderActivity);
    //支付回调orderA and actOrder修改表
    ServiceResponse<String> updatePayBack(String outTradeNo);
}

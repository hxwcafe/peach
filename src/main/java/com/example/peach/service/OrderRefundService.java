package com.example.peach.service;

import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.OrderRefund;
import com.example.peach.pojo.merge.OrderRAct;

import java.util.List;

public interface OrderRefundService {
    //插入退款数据
    ServiceResponse<Integer> insertOrderRefund(Integer userId);
    //查询所有退款订单
    List<OrderRAct> selectallOrderRAct();
    //删除退款订单
    ServiceResponse<String> deleteRefund(Integer id);
    //修改退款订单
    ServiceResponse<String> updateOrderRefund(OrderRefund orderRefund);
    //修改不合格订单
    ServiceResponse<String> upRefundandUserdeposit(OrderRefund orderRefund);
    //押金退款
    ServiceResponse RefundPay(String out_trade_no, double total_fee,OrderRefund orderRefund);
    //所有待处理订单
    ServiceResponse<List<OrderRefund>> allclRefund();
    //自动退款
    void automaticRefund();
}

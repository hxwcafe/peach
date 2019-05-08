package com.example.peach.service;

import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.Orderpay;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface OrderPayService {
    //插入订单信息
    int insertOrderPay(Orderpay orderpay);
    //根据商户订单号修改订单信息
    int updateOrder_statusByout_trade_no(Orderpay orderpay);
    //根据微信订单号修改订单信息
    int updateOrder_statusBytrade_status(Orderpay orderpay);
    //根据用户id查询订单
    ServiceResponse<List> selectOrderByuserId(int user_id);
    //删除订单
    int delectByoutTradeNo(String outTradeNo);
    //会员购买
    ServiceResponse<Map> Pricerevision(String openid, int id) throws ParseException;
    //更具用户outtradeno(商户信息)查询信息
    Orderpay selectByouttradeno(String outTradeNo);

}

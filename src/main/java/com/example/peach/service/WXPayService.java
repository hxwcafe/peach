package com.example.peach.service;

import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.Activity;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

public interface WXPayService {

    Map actualMoney(String openid, Activity activity);
    /**
     * 支付付款
     * @param mapinfo
     * @return
     * @throws IllegalAccessException
     */
    ServiceResponse<Map> paymettwe(Map mapinfo, HttpServletRequest request) throws IllegalAccessException;

    //回调逻辑
    void wxpayBackmysql(String openid, String out_trade_no, Double cash_fee) throws ParseException;

    /**
     *  微信查询订单
     * @param out_trade_no 商户订单号
     * @return
     */
    ServiceResponse<Object> orderQueryZT(String out_trade_no);

    /**
     * 订单关闭
     * @param out_trade_no
     * @return
     */
    ServiceResponse<Object> ClosePay(String out_trade_no);

    /**
     *  30分钟内没有支付,自动关闭订单
     * @param out_trade_no 订单号
     */
    void orderClose(String out_trade_no);
}

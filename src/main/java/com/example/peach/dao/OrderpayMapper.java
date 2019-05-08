package com.example.peach.dao;

import com.example.peach.pojo.Orderpay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderpayMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Orderpay record);

    int insertSelective(Orderpay record);

    Orderpay selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(Orderpay record);
    //根据用户微信订单号查询订单信息
    int updateOrder_statusBytrade_status(Orderpay orderpay);
    //根据用户商户号查询订单信息
    int updateOrder_statusByout_trade_no(Orderpay orderpay);
    //根据用户id查询订单历史
    List<Orderpay> selectOrderByuserid(int user_id);
    //关闭订单,删除订单信息
    int deleteByout_trade_no(String outTradeNo);

    Orderpay selectByUseridAndCommodityid(@Param(value = "userid") int userid, @Param(value = "commodityid") int commodityid, @Param(value = "tradeStatus") String tradeStatus);
    //根据商户号查询订单信息
    Orderpay selectByouttradeno(String outTradeNo);
}
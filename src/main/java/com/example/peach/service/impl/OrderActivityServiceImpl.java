package com.example.peach.service.impl;

import com.example.peach.common.ServiceResponse;
import com.example.peach.dao.OrderActivityMapper;
import com.example.peach.pojo.OrderActivity;
import com.example.peach.pojo.UserVip;
import com.example.peach.service.OrderActivityService;
import com.example.peach.service.UserVipService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderActivityServiceImpl implements OrderActivityService {
    @Resource
    private OrderActivityMapper orderActivityMapper;
    @Resource
    private UserVipService userVipService;
    /**
     *  插入活动订单信息
     * @param orderActivity 活动订单
     * @return
     */
    @Override
    public ServiceResponse<String> insertOrderActivity(OrderActivity orderActivity) {

        int getrows = orderActivityMapper.insert(orderActivity);
        if(getrows>0){
            return ServiceResponse.createBySuccess("插入成功");
        }else{
            return ServiceResponse.createByError("插入失败");
        }
    }

    /**
     * 根据商户订单查询商户信息
     * @param out 商户订单号
     * @return 商户订单信息
     */
    @Override
    public ServiceResponse<OrderActivity> selectOrderActivity(String out) {
        OrderActivity orderActivity = orderActivityMapper.selectOrderUAByOut(out);
        if(orderActivity!=null) {
            return ServiceResponse.createBySuccess(orderActivity);
        }
        return ServiceResponse.createByError();
    }

    /**
     * 根据out动态修改活动订单信息
     * @param orderActivity 活动订单实体类
     * @return true false
     */
    @Override
    public ServiceResponse<String> updateByOutTradeNo(OrderActivity orderActivity) {
        //业务逻辑
        int getrows = orderActivityMapper.updateByOutTradeNo(orderActivity);
        if(getrows>0){
          return ServiceResponse.createBySuccess();
        }
        return ServiceResponse.createByError();
    }

    /**
     * 回调修改订单信息
     * @param outTradeNo
     * @return
     */
    @Override
    public ServiceResponse<String> updatePayBack(String outTradeNo) {
       int status =2;
       String tradeStatus = "SUCCESS";
        int getrows = orderActivityMapper.updatePayBack(outTradeNo,status,tradeStatus);
        if(getrows>0){
            OrderActivity orderActivity = orderActivityMapper.selectOrderUAByOut(outTradeNo);
            UserVip userVip = new UserVip();
            userVip.setUserId(orderActivity.getUserId());
            userVip.setUserDeposit(orderActivity.getDepositFee());
           ServiceResponse uservipResonse =  userVipService.updateUdepositByUserId(userVip);
           if(uservipResonse.isSuccess()) {
               return ServiceResponse.createBySuccess();
           }else{
               return ServiceResponse.createByError();
           }
        }
        return ServiceResponse.createByError();
    }


}

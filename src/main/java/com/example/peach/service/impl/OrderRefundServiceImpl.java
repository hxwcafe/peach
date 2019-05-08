package com.example.peach.service.impl;

import com.example.peach.common.ServiceResponse;
import com.example.peach.configuration.Configure;
import com.example.peach.dao.OrderActivityMapper;
import com.example.peach.dao.OrderRefundMapper;
import com.example.peach.dao.OrderpayMapper;
import com.example.peach.dao.UserVipMapper;
import com.example.peach.pojo.OrderActivity;
import com.example.peach.pojo.OrderRefund;
import com.example.peach.pojo.Orderpay;
import com.example.peach.pojo.UserVip;
import com.example.peach.pojo.merge.OrderRAct;
import com.example.peach.pojo.merge.UvipUser;
import com.example.peach.service.OrderPayService;
import com.example.peach.service.OrderRefundService;
import com.example.peach.util.CertUtil;
import com.example.peach.util.DateSub;
import com.example.peach.util.PayUtil;
import com.example.peach.util.RandomStringGenerator;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class OrderRefundServiceImpl implements OrderRefundService {
    @Resource
    private OrderRefundMapper orderRefundMapper;
    @Resource
    private UserVipMapper userVipMapper;
    @Resource
    private OrderpayMapper orderpayMapper;
    @Resource
    private OrderActivityMapper orderActivityMapper;
    /**
     * 插入退款数据逻辑
     * @param userId 用户id
     * @return true false(0 没有缴纳押金,1 您已经有一个退款正在处理中,2 退款插入失败)
     */
    @Override
    public ServiceResponse<Integer> insertOrderRefund(Integer userId) {
        OrderRefund orderRefund = new OrderRefund();
        orderRefund.setUserId(userId);
        UvipUser uvipUser = userVipMapper.selectUvipUser(userId);
        //是否有押金
        if (uvipUser.getUserDeposit() >= (double) 50) {
            OrderRAct orderRAct = orderRefundMapper.selectorderacticityId(userId);
            OrderRefund pdstatus = orderRefundMapper.selectStatusByuseerId(userId);
            if (orderRAct != null) {
                if (pdstatus != null) {
                    if (pdstatus.getStatus() != 0) {
                        orderRefund.setOrderacticityId(orderRAct.getOrderacticityId());
                        return this.insertOrderRefund(orderRefund);
                    }
                    return ServiceResponse.createByError(1);
                } else {
                    orderRefund.setOrderpayId(orderRAct.getOrderpayId());
                    return insertOrderRefund(orderRefund);
                }
            } else {
                OrderRAct orderRActPay = orderRefundMapper.selectorderpayId(userId);
                //是否有购买押金订单
                if (orderRActPay != null) {
                    //是否有申请过退款
                    if (pdstatus != null) {
                        //退款是否在处理中
                        if (pdstatus.getStatus() != 0) {
                            orderRefund.setOrderpayId(orderRActPay.getOrderpayId());
                            return insertOrderRefund(orderRefund);
                        }
                        return ServiceResponse.createByError(1);
                    } else {
                        orderRefund.setOrderpayId(orderRActPay.getOrderpayId());
                        return insertOrderRefund(orderRefund);
                    }
                } else {
                    return ServiceResponse.createByError(0);
                }
            }
        } else {
            return ServiceResponse.createByError(0);
        }
    }

    /**
     * 查询所有退款订单
     * @return true(List OrderRAct) false
     */
    @Override
    public List<OrderRAct> selectallOrderRAct() {
        List<OrderRAct> orderRActList = orderRefundMapper.selectOrderRAct();
        if(orderRActList!=null) {
            for (OrderRAct orderRAct : orderRActList) {
                if (orderRAct.getOrderpayId() != null) {
                    Orderpay orderpay = orderpayMapper.selectByPrimaryKey(orderRAct.getOrderpayId());
                    orderRAct.setOrderpay(orderpay);
                }else if (orderRAct.getOrderacticityId() != null) {
                    OrderActivity orderActivity = orderActivityMapper.selectByPrimaryKey(orderRAct.getOrderacticityId());
                    orderRAct.setOrderActivity(orderActivity);
                }
            }
            return orderRActList;
        }
        return null;
    }

    /**
     * 插入退款数据
     * @param orderRefund 退款表实体类
     * @return true false
     */
    public ServiceResponse insertOrderRefund(OrderRefund orderRefund) {
        String nonceStr = RandomStringGenerator.getRandomStringtime(32);//退款单号
        orderRefund.setOutRefundNo(nonceStr);
        int getrows = orderRefundMapper.insert(orderRefund);
        if (getrows > 0) {
            return ServiceResponse.createBySuccess();
        } else {
            return ServiceResponse.createByError("退款插入失败");
        }
    }

    /**
     * 删除退款订单
     * @param id 订单id
     * @return true false
     */
    @Override
    public ServiceResponse<String> deleteRefund(Integer id){
        int getrows = orderRefundMapper.deleteByPrimaryKey(id);
        if(getrows>0){
            return ServiceResponse.createBySuccess("删除成功");
        }
        return ServiceResponse.createByError("删除失败");
    }

    /**
     * 修改退款订单
     * @param orderRefund 退款表
     * @return true false
     */
    @Override
    public ServiceResponse<String> updateOrderRefund(OrderRefund orderRefund){
        int getrows = orderRefundMapper.updateByPrimaryKeySelective(orderRefund);
        if(getrows>0){
            return ServiceResponse.createBySuccess("修改成功");
        }
        return ServiceResponse.createByError("修改失败");
    }
    //修改订单数据,不合格
    @Override
    public ServiceResponse<String> upRefundandUserdeposit(OrderRefund orderRefund){
        ServiceResponse<String> updateRefund = this.updateOrderRefund(orderRefund);
        if (updateRefund.isSuccess()) {
            UserVip userVip = new UserVip();
            Double deposit = Double.valueOf(50);
            deposit = userVipMapper.selectByUserId(orderRefund.getUserId()).getUserDeposit() - deposit;
            userVip.setUserId(orderRefund.getUserId());
            userVip.setUserDeposit(deposit);
            userVipMapper.updateByPrimaryKeySelective(userVip);
            return ServiceResponse.createByError("修改成功");
        }
        return ServiceResponse.createByError("修改失败!");
    }
    //押金退款
    @Override
    public ServiceResponse<String> RefundPay(String out_trade_no, double total_fee,OrderRefund orderRefund) {
        long money=Math.round(total_fee*100);
        String param = PayUtil.wxPayRefund(out_trade_no, String.valueOf(money),orderRefund.getOutRefundNo());
        System.out.println("支付退款签名:" + param);
        String result = "";
        try {
            result = CertUtil.refund(param, Configure.Refund_URL);
            System.out.println(result);
            Map paraminfo = PayUtil.doXMLParse(result);//xml转map
            if ("SUCCESS".equals(paraminfo.get("return_code"))) {
                if ("SUCCESS".equals(paraminfo.get("result_code"))) {
                    //退款成功的操作
                    String prepay_id = (String) paraminfo.get("prepay_id");//返回的预付单信息
                    System.out.println(prepay_id);
                    //修改OrderRefund
                    orderRefund.setRefundStatus(1);
                    ServiceResponse refundResponse = this.upRefundandUserdeposit(orderRefund);
                    return refundResponse;
                } else {
                    System.out.println("退款失败:原因" + paraminfo.get("err_code_des"));
                    orderRefund.setRefundStatus(0);
                    orderRefund.setStatus(3);
                    this.updateOrderRefund(orderRefund);
                    return ServiceResponse.createByError((String) paraminfo.get("err_code_des"));
                }
            } else {
                System.out.println("退款失败:原因" + paraminfo.get("return_msg"));
                orderRefund.setRefundStatus(0);
                orderRefund.setStatus(3);
                this.updateOrderRefund(orderRefund);
                return ServiceResponse.createByError((String) paraminfo.get("return_msg"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Pattern p = Pattern.compile("");
        return ServiceResponse.createByError(null);
    }

    /**
     * 所有待处理的订单
     * @return true(List OrderRefund) false
     */
    @Override
    public ServiceResponse<List<OrderRefund>> allclRefund(){
       List<OrderRefund> orderRefundLis = orderRefundMapper.selectallByStatus();
       if(orderRefundLis!=null){
          return ServiceResponse.createBySuccess(orderRefundLis);
       }
       return ServiceResponse.createByError(orderRefundLis);
    }

    /**
     * 订单待处理超过48小时自动退款
     */
    @Override
    public void automaticRefund() {
        ServiceResponse<List<OrderRefund>> RefundResponse = this.allclRefund();
        if (RefundResponse.isSuccess()) {
            for (OrderRefund orderRefund : RefundResponse.getData()) {
                //时间超过48小时自动退款
                long hour = DateSub.getHourSub(orderRefund.getCreateTime());
                if (hour >= 48) {
                    if (orderRefund.getOrderpayId() != null) {
                        Orderpay orderpay = orderpayMapper.selectByPrimaryKey(orderRefund.getOrderpayId());
                        this.RefundPay(orderpay.getOutTradeNo(),orderpay.getTotalFee(),orderRefund);
                    } else if (orderRefund.getOrderacticityId() != null) {
                        OrderActivity orderActivity = orderActivityMapper.selectByPrimaryKey(orderRefund.getOrderacticityId());
                        this.RefundPay(orderActivity.getOutTradeNo(),orderActivity.getDepositFee(),orderRefund);
                    }
                }
            }
        }
    }
}

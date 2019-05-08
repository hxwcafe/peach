package com.example.peach.pojo;

import java.util.Date;

public class OrderRefund {
    private Integer id;
    //充值订单id
    private Integer orderpayId;
    //活动订单id
    private Integer orderacticityId;
    //用户id
    private Integer userId;
    //退款单号
    private String outRefundNo;
    //退款状态
    private Integer refundStatus;
    //订单状态 0待处理,1完成,2不合格
    private Integer status;
    //创建时间
    private Date createTime;
    //结束时间
    private Date endTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderacticityId() {
        return orderacticityId;
    }

    public void setOrderacticityId(Integer orderacticityId) {
        this.orderacticityId = orderacticityId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getOrderpayId() {
        return orderpayId;
    }

    public void setOrderpayId(Integer orderpayId) {
        this.orderpayId = orderpayId;
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public Integer getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(Integer refundStatus) {
        this.refundStatus = refundStatus;
    }
}
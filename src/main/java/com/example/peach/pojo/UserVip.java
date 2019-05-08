package com.example.peach.pojo;

import java.util.Date;

/**
 * 用户vip表
 */
public class UserVip {
    private Integer id;
    //等级
    private Integer vipGrade;
    //约见次数
    private Integer vipAppointment;
    //会员办理时间
    private Date vipCreatetime;
    //会员过期时间
    private Date vipEndtime;
    //用户id
    private Integer userId;
    //钱包金额
    private Double userWallet;
    //押金
    private Double userDeposit;
    //免费参加
    private Boolean userFree;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVipGrade() {
        return vipGrade;
    }

    public void setVipGrade(Integer vipGrade) {
        this.vipGrade = vipGrade;
    }

    public Integer getVipAppointment() {
        return vipAppointment;
    }

    public void setVipAppointment(Integer vipAppointment) {
        this.vipAppointment = vipAppointment;
    }

    public Date getVipCreatetime() {
        return vipCreatetime;
    }

    public void setVipCreatetime(Date vipCreatetime) {
        this.vipCreatetime = vipCreatetime;
    }

    public Date getVipEndtime() {
        return vipEndtime;
    }

    public void setVipEndtime(Date vipEndtime) {
        this.vipEndtime = vipEndtime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Double getUserWallet() {
        return userWallet;
    }

    public void setUserWallet(Double userWallet) {
        this.userWallet = userWallet;
    }

    public Double getUserDeposit() {
        return userDeposit;
    }

    public void setUserDeposit(Double userDeposit) {
        this.userDeposit = userDeposit;
    }

    public Boolean getUserFree() {
        return userFree;
    }

    public void setUserFree(Boolean userFree) {
        this.userFree = userFree;
    }
}
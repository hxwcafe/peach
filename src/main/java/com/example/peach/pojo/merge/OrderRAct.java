package com.example.peach.pojo.merge;

import com.example.peach.pojo.*;

import java.util.List;

public class OrderRAct extends OrderRefund {
    OrderActivity orderActivity;

    Orderpay orderpay;

    UserVip userVip;

    User user;

    public OrderActivity getOrderActivity() {
        return orderActivity;
    }

    public void setOrderActivity(OrderActivity orderActivity) {
        this.orderActivity = orderActivity;
    }

    public Orderpay getOrderpay() {
        return orderpay;
    }

    public void setOrderpay(Orderpay orderpay) {
        this.orderpay = orderpay;
    }

    public UserVip getUserVip() {
        return userVip;
    }

    public void setUserVip(UserVip userVip) {
        this.userVip = userVip;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

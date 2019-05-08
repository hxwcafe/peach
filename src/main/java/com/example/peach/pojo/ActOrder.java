package com.example.peach.pojo;


import java.sql.Timestamp;
import java.util.List;

/**
    活动订单
 * Created by Administrator on 2018/11/17.
 */
public class ActOrder {
    private Integer id; //主键id
    private Integer activityId; //活动id
    private List<Activity> activityList; //活动表
    private Integer userId;//用户id
    private  List<User> userList;//user表信息
    private Timestamp orderNumber;//活动订单时间
    private Integer status; //活动状态
    private  Integer signIn;//活动签到
//    private  List<Album> albumList;//用户相册

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Timestamp getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Timestamp orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Activity> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<Activity> activityList) {
        this.activityList = activityList;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public Integer getSignIn() {
        return signIn;
    }

    public void setSignIn(Integer signIn) {
        this.signIn = signIn;
    }

//    public List<Album> getAlbumList() {
//        return albumList;
//    }
//
//    public void setAlbumList(List<Album> albumList) {
//        this.albumList = albumList;
//    }
}
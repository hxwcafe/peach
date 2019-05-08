package com.example.peach.pojo;

import java.sql.Timestamp;

/**   签到
 * Created by Administrator on 2018/12/6.
 */
public class SignIn {

    private  Integer id;
    private Timestamp signInTime; //签到时间
    private  Integer  signInNumber;//签到次数
    private  Integer userId;   //用户id
    private  Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getSignInTime() {
        return signInTime;
    }

    public void setSignInTime(Timestamp signInTime) {
        this.signInTime = signInTime;
    }

    public Integer getSignInNumber() {
        return signInNumber;
    }

    public void setSignInNumber(Integer signInNumber) {
        this.signInNumber = signInNumber;
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
}

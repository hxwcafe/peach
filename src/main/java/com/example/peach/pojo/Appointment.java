package com.example.peach.pojo;


import com.example.peach.pojo.merge.UAlbum;

import java.sql.Timestamp;
import java.util.List;

/**
 *  约见表
 * Created by Administrator on 2018/11/21.
 */
public class Appointment {
    private int id;//主键
    private int myid;//自己
    private int youid;//约见对象
    private int status;//状态
    private Timestamp createTime;//创建时间
    private int managerStatus;//管理员处理状态
    private String managerString;//状态描述
    private List<UAlbum> list;

    public List<UAlbum> getList() {
        return list;
    }

    public void setList(List<UAlbum> list) {
        this.list = list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMyid() {
        return myid;
    }

    public void setMyid(int myid) {
        this.myid = myid;
    }

    public int getYouid() {
        return youid;
    }

    public void setYouid(int youid) {
        this.youid = youid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public int getManagerStatus() {
        return managerStatus;
    }

    public void setManagerStatus(int managerStatus) {
        this.managerStatus = managerStatus;
    }

    public String getManagerString() {
        return managerString;
    }

    public void setManagerString(String managerString) {
        this.managerString = managerString;
    }
}


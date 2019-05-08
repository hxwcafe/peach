package com.example.peach.pojo;

/**
 * 获取为token表
 * Created by Administrator on 2018/11/15.
 */
public class Token {

    private  int id;//主键
    private  String access_token;//微信接口token码

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}

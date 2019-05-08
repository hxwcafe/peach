package com.example.peach.service;

import com.example.peach.pojo.Token;

/**
 * Created by Administrator on 2018/11/16.
 */

public interface TokenService {


    //查询
    Token selectById();

    //修改
    int updateById(String access_token);


}

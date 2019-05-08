package com.example.peach.service;

import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.Mate;
import com.example.peach.pojo.merge.UAMate;

/**
 * Created by Administrator on 2018/11/22.
 */
public interface MateService {
    Mate selectByuserId(Integer userId);

    ServiceResponse<String> insertMate(Mate mate);

    ServiceResponse<String> updateMate(Mate mate);

    Mate selectByOpenId(String openid);

    //根据userId查询用户基本信息,择偶要求
    ServiceResponse<UAMate> selectUAMate(Integer userId);
    //根据userId删除Mate
    ServiceResponse<String> delectmate(Integer userId);
}

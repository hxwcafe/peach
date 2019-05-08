package com.example.peach.controller;

import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.Token;
import com.example.peach.service.TokenService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2018/11/16.
 */
@RestController
@RequestMapping(value = "/get")
public class GetTokenController {

    @Resource
    private TokenService tokenService;

    @RequestMapping(value = "/token",  method = RequestMethod.GET)
    public ServiceResponse GetToken(){

        Token token=tokenService.selectById();
        if (token!=null){
            return ServiceResponse.createBySuccess(token);
        }else {
            return ServiceResponse.createByError("获取失败！");
        }
    }
}

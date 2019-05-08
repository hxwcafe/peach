package com.example.peach.service;


import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.SignIn;

import java.util.Map;

/**
 * Created by Administrator on 2018/12/6.
 */
public interface SignInService {

    ServiceResponse<Map> selectByuserId(Integer userId);

    ServiceResponse<Map> insertSignIn(SignIn signIn);

    ServiceResponse<Map> updateSignIn(SignIn signIn);
}

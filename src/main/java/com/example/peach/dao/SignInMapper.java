package com.example.peach.dao;

import com.example.peach.pojo.SignIn;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/12/6.
 */
@Component
@Mapper
public interface SignInMapper {
    SignIn selectByuserId(Integer userId);

    int insertSignIn(SignIn signIn);

    int updateSignIn(SignIn signIn);

}

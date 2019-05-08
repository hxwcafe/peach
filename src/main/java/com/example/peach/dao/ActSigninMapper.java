package com.example.peach.dao;

import com.example.peach.pojo.ActSignin;

public interface ActSigninMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ActSignin record);

    int insertSelective(ActSignin record);

    ActSignin selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ActSignin record);

    int updateByPrimaryKey(ActSignin record);
}
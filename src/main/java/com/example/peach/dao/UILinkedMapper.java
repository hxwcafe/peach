package com.example.peach.dao;

import com.example.peach.pojo.UILinked;

import java.util.List;

public interface UILinkedMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UILinked record);

    UILinked selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(UILinked record);
    //更具用户id查询兴趣id
    List<UILinked> selectByUserId(Integer userId);
    //更具兴趣id查询用户数量
    Integer selectCountByinterest_id(Integer id);
}
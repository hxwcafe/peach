package com.example.peach.dao;

import com.example.peach.pojo.Token;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/11/16.
 */

@Component
@Mapper
public interface TokenMapper {


    //查询
    Token selectById(int id);

    //修改
    int updateById(String access_token);
}

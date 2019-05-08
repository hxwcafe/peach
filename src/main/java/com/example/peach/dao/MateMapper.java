package com.example.peach.dao;

import com.example.peach.pojo.Mate;
import com.example.peach.pojo.merge.UAMate;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/11/22.
 */
@Component
@Mapper
public interface MateMapper {
    Mate selectByuserId(Integer userId);

    int insertMate(Mate mate);

    int updateMate(Mate mate);

    Mate selectByOpenId(String openid);
    //根据userid,获取Alum,Mate,Uservip,User
    UAMate selectUAMate(Integer userId);
    //根据用户id删除
    int deleteByPrimaryKey(Integer userId);
}

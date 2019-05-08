package com.example.peach.dao;

import com.example.peach.pojo.ActOrder;
import com.example.peach.pojo.merge.UAlbum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Administrator on 2018/11/17.
 */
@Component
@Mapper
public interface ActOrderMapper {
    ActOrder getActOrder(Integer id);

    int insertActOrder(ActOrder actOrder);

    List<ActOrder> selectActOrderList(Integer activityId);

    int updateActOrder(ActOrder actOrder);

    List<ActOrder> selectUserId(Integer userId);

    ActOrder selectById(Integer id);

    List<ActOrder> selectByActId(Integer activityId);

    List<UAlbum> selectUAlbum(Integer activityId);

    ActOrder selectStatusByUAid(@Param(value = "userId") Integer userId, @Param(value = "activityId") Integer activityId);

    int updatemanagerAct(ActOrder actOrder);

    int deleteActOrderById(Integer id);

}

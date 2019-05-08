package com.example.peach.dao;

import com.example.peach.pojo.MovieInvitation;
import com.example.peach.pojo.merge.MovieMYuser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MovieInvitationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MovieInvitation record);

    int insertSelective(MovieInvitation record);

    MovieInvitation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MovieInvitation record);
    //修改status
    int updateByPrimaryKey(MovieInvitation record);
    //根据myid,youid,activityId查询status
    MovieInvitation selectStatusByMYA(@Param(value = "myid") Integer myid, @Param(value = "youid") Integer youid, @Param(value = "activityId") Integer activityId);
    //根据myid,activityId查询status
    List<MovieInvitation> selectStatusByMA(@Param(value = "myid") Integer myid, @Param(value = "activityId") Integer activityId);
    //根据youid,activityId查询status
    List<MovieInvitation> selectStatusByYA(@Param(value = "youid") Integer youid, @Param(value = "activityId") Integer activityId);
    //根据活动id查询活动订单所有消息
    List<MovieMYuser> selectMovieMYuser(Integer activityId);
}
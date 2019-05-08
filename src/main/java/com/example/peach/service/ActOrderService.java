package com.example.peach.service;

import com.example.peach.common.Pager;
import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.ActOrder;
import com.example.peach.pojo.MovieInvitation;
import com.example.peach.pojo.merge.MovieMYuser;
import com.example.peach.pojo.merge.UAlbum;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Administrator on 2018/11/17.
 */
public interface ActOrderService {
    ActOrder getActOrder(Integer id);

    ServiceResponse<Integer> insertActOrder(ActOrder actOrder);

    List<ActOrder> selectActOrderList(Integer activityId);

    ServiceResponse<String> updateActOrder(ActOrder actOrder,Double longitude,Double dimension);

    ServiceResponse<List> selectUserId(Integer userId) throws ParseException;

    ActOrder selectById(Integer id);

    List<ActOrder> selectByActId(Integer activityId);

    ServiceResponse<Pager<UAlbum>> selectUAlbum(Integer activityId, Integer pageIndex);

    ServiceResponse<ActOrder> selectStatusByUAid(Integer userId, Integer activityId);
    //活动邀请
    ServiceResponse<String> addInvitation(MovieInvitation movieInvitation);
    //活动邀请同意
    ServiceResponse<Integer> movieAgree(MovieInvitation movieInvitation);
    //活动邀请同意
    ServiceResponse<String> movieRefuse(MovieInvitation movieInvitation);
    //邀请状态
    ServiceResponse<Integer> movieStatus(Integer myid, Integer youid, Integer activityId);

    List<UAlbum> selectUAlbum(Integer activityId);

    List<MovieMYuser> selectMovieMYuser(Integer activityId);
    //修改活动订单
    ServiceResponse<String> updateManagerOrder(ActOrder actOrder);
    //删除活动订单
    ServiceResponse<String> delectActOrder(Integer id);
}

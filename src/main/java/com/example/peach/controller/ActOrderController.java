package com.example.peach.controller;

import com.example.peach.common.Pager;
import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.ActOrder;
import com.example.peach.pojo.MovieInvitation;
import com.example.peach.pojo.merge.UAlbum;
import com.example.peach.service.ActOrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/11/17.
 */
@RestController
@RequestMapping("/actorder")
public class ActOrderController {
    @Resource
     private ActOrderService actOrderService;
    /**
     * 查询活动单
     * @param id 活动id
     * @return map 所有活动信息
     */
    @RequestMapping(value = "/getactorder",method = RequestMethod.POST)
    public Map<String,Object> getActOrder(@RequestParam Integer id){
        ActOrder actOrder = actOrderService.getActOrder(id);
        Map<String,Object> map=new HashMap<>();
        map.put("actOrder",actOrder);
        return map;
    }
    /**
     * 添加活动单
     */
    @RequestMapping(value = "/insertactorder",method = RequestMethod.POST)
    public ServiceResponse<Integer> insertActOrder(@ModelAttribute ActOrder actOrder){
        return actOrderService.insertActOrder(actOrder);
    }

    /**
     * 查询该用户该活动,活动信息
     * @param userId 用户id
     * @param activityId 活动id
     * @return true(ActOrder)  false
     */
    @RequestMapping(value = "/queryactorder",method = RequestMethod.GET)
    public ServiceResponse<ActOrder> selectStatusByUAid(Integer userId, Integer activityId){
        return actOrderService.selectStatusByUAid(userId,activityId);
    }
    /**
     * 根据activityId查询所有用户活动行程
     */
    @RequestMapping(value = "/selectActOrderList",method = RequestMethod.GET)
    public Map<String,Object> selectActOrderList(@RequestParam Integer activityId){
        List<ActOrder> list= actOrderService.selectActOrderList(activityId);
        Map<String,Object> map=new HashMap<>();
        map.put("list",list);
        return map;
    }
    /**
     * 根据userId查询活动单
     */
    @RequestMapping(value = "/selectuseridlist",method = RequestMethod.GET)
    public ServiceResponse<List> selectUserId(@RequestParam Integer userId) throws ParseException {
        return actOrderService.selectUserId(userId);
    }
    /**
     * 根据id查询
     */
    @RequestMapping(value = "/selectById",method = RequestMethod.GET)
    public Map<String,Object> selectById(@RequestParam Integer id) {
        ActOrder actOrder = actOrderService.selectById(id);
        Map<String, Object> map = new HashMap<>();
        map.put("actOrder", actOrder);
        return map;
    }

    /**
     * 根据activityId查询用户和相册
     */
    @RequestMapping(value = "/selectualbuem",method = RequestMethod.GET)
    public ServiceResponse<Pager<UAlbum>> selectUAlbuem(@RequestParam Integer activityId, Integer pageIndex){
        return actOrderService.selectUAlbum(activityId,pageIndex);
    }

    /**
     * 活动邀请
     * @param movieInvitation myid 自己id,youid 对方id,activityId 活动id
     * @return 是否邀请成功 是否报名
     */
    @RequestMapping(value = "addinvitation",method = RequestMethod.POST)
    public ServiceResponse<String> addInvitation(MovieInvitation movieInvitation){
        return actOrderService.addInvitation(movieInvitation);
    }

    /**
     * 接受邀请
     * @param movieInvitation myid,youid activityId
     * @return true false
     */
    @RequestMapping(value = "agree",method = RequestMethod.POST)
    public ServiceResponse<Integer> movieAgree(MovieInvitation movieInvitation){
        movieInvitation.setStatus(2);
        return actOrderService.movieAgree(movieInvitation);
    }
    /**
     * 拒接邀请
     * @param movieInvitation myid,youid activityId
     * @return true false
     */
    @RequestMapping(value = "refuse",method = RequestMethod.POST)
    public ServiceResponse<String> movieRefuse(MovieInvitation movieInvitation){
        movieInvitation.setStatus(1);
        return actOrderService.movieRefuse(movieInvitation);
    }
    /**
     *  本周活动签到
     */
        @RequestMapping(value = "/updatesignin",method = RequestMethod.POST)
    public ServiceResponse<String> updateSignIn(@ModelAttribute ActOrder actOrder,String longitude,String dimension){
        actOrder.setSignIn(1);
        return actOrderService.updateActOrder(actOrder,Double.parseDouble(longitude),Double.parseDouble(dimension));
    }

    /**
     * 邀约状态
     * @param myid       自己id
     * @param youid      被邀请id
     * @param activityId 活动id
     * @return msg[0(邀请中), 1(已拒绝), 2(已同意), 3(邀请), 4(同意和拒绝), 5(已被邀请)]
     */
    @RequestMapping(value = "/status",method = RequestMethod.GET)
    public ServiceResponse<Integer> movieStatus(Integer myid, Integer youid, Integer activityId){
       return actOrderService.movieStatus(myid,youid,activityId);
    }

}

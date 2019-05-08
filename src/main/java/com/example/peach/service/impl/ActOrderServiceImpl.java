package com.example.peach.service.impl;

import com.example.peach.common.Pager;
import com.example.peach.common.ServiceResponse;
import com.example.peach.dao.*;
import com.example.peach.pojo.*;
import com.example.peach.pojo.merge.MovieMYuser;
import com.example.peach.pojo.merge.UAlbum;
import com.example.peach.pojo.merge.UvipUser;
import com.example.peach.service.ActOrderService;
import com.example.peach.service.UserService;
import com.example.peach.service.UserVipService;
import com.example.peach.util.AmountUtils;
import com.example.peach.util.DateSub;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/11/17.
 */
@Service
public class ActOrderServiceImpl implements ActOrderService {
    @Resource
    private ActOrderMapper actOrderMapper;
    @Resource
    private ActivityMapper activityMapper;
    @Resource
    private UserService userService;
    @Resource
    private UserVipService userVipService;
    @Resource
    private MovieInvitationMapper movieInvitationMapper;
    @Resource
    private ActSigninMapper actSigninMapper;

    /**
     * 根据id查询活动单
     *
     * @param id
     * @return
     */
    @Override
    public ActOrder getActOrder(Integer id) {
        return actOrderMapper.getActOrder(id);
    }

    /**
     * 添加活动单(报名)
     *
     * @param actOrder
     * @return
     */
    @Override
    @Transactional
    public ServiceResponse<Integer> insertActOrder(ActOrder actOrder) {
            ServiceResponse uvipUserResponse = userVipService.selectUvipUser(actOrder.getUserId());
            UvipUser uvipUser = (UvipUser) uvipUserResponse.getData();
            if(uvipUser.getUser().getUserBlacklist()==1){//查看用户是否是黑名单
                return ServiceResponse.createByError(5);
            }
            //是否有免费参加
            if (uvipUser.getUserFree()) {
                ServiceResponse<Integer> bmResponse = this.baoming(actOrder);
                if (bmResponse.isSuccess()) {
                    uvipUser.setUserFree(false);
                    //修改userFree(免费参与)
                    ServiceResponse xgUserFree = userVipService.updateUserVip(uvipUser);
                    if (xgUserFree.isSuccess()) {
                        return bmResponse;
                    }
                    return bmResponse;
                }
                return bmResponse;
            }
            //判断是否是会员
            ServiceResponse vipResponse = userVipService.selectCreatAndEndByUserId(actOrder.getUserId());
            if (vipResponse.isSuccess()) {
                ServiceResponse<Integer> bmResponse = this.baoming(actOrder);
                return bmResponse;
            }
            return vipResponse;
    }

    //报名
    public ServiceResponse<Integer> baoming(ActOrder actOrder) {
        Activity activity = activityMapper.selectById(actOrder.getActivityId());
        //当前时间与活动开始时间的天数差
        Long longs = DateSub.getDaySub(activity.getActtime());
        if (longs >= 1) {
            ActOrder actOrder1 = actOrderMapper.selectStatusByUAid(actOrder.getUserId(), actOrder.getActivityId());
            if (actOrder1 == null) {
                int insert = actOrderMapper.insertActOrder(actOrder);
                if (insert > 0) {
                    return ServiceResponse.createBySuccess("报名成功");
                } else {
                    return ServiceResponse.createByError(3);
                }
            }
            return ServiceResponse.createByError(4);

        }
        return ServiceResponse.createByError(3);
    }

    /**
     * 根据活动单id查询所有活动
     *
     * @param activityId
     * @return
     */

    @Override
    public List<ActOrder> selectActOrderList(Integer activityId) {
        return actOrderMapper.selectActOrderList(activityId);
    }
    //本周活动签到
    @Override
    public ServiceResponse<String> updateActOrder(ActOrder actOrder,Double longitude,Double dimension) {
        Activity activity = activityMapper.selectById(actOrder.getActivityId());
        Long longs = DateSub.getMinSub(activity.getActtime());
        //判断是否在时间内
        if (longs<=180) {
            if(longs>=-120) {
                ActSignin actSignin = actSigninMapper.selectByPrimaryKey(1);
                longitude=AmountUtils.doubleRetain(longitude,3);
                dimension=AmountUtils.doubleRetain(dimension,3);
                //判断是否在这经纬度
                if(actSignin.getLongitude()<= (longitude+0.01)&actSignin.getLongitude()>= (longitude-0.01)&
                        actSignin.getDimension()>= (dimension-0.01)&actSignin.getDimension()<= (dimension+0.01)) {
                    int update = actOrderMapper.updateActOrder(actOrder);
                    if (update > 0) {
                        return ServiceResponse.createBySuccess("签到成功");
                    }
                }else{
                    return ServiceResponse.createByError("还未到签到地点");
                }
            }else{
                return ServiceResponse.createByError("签到已提前");
            }
        }else{
            return ServiceResponse.createByError("签到已过时");
        }
        return ServiceResponse.createByError("签到失败");
    }

    /**
     * 根据userId查询所有活动单
     *
     * @param userId
     * @return
     */
    @Override
    public ServiceResponse<List> selectUserId(Integer userId) throws ParseException {
        List<ActOrder> list = actOrderMapper.selectUserId(userId);
        if (list != null) {
//            List<ActOrder> actOrderList = new ArrayList<ActOrder>();
            //活动过期不显示
//            for (ActOrder actOrder : list) {
//                if (DateSub.getDaySub(actOrder.getActivityList().get(0).getActendtime()) >= 0) {
//                    actOrderList.add(actOrder);
//                }
//            }
            return ServiceResponse.createBySuccess(list);
        } else {
            return ServiceResponse.createByError("没有行程活动");
        }

    }


    /**
     * 根据活动单id查询
     *
     * @param id
     * @return
     */
    @Override
    public ActOrder selectById(Integer id) {
        return actOrderMapper.selectById(id);
    }

    @Override
    public List<ActOrder> selectByActId(Integer activityId) {
        List<ActOrder> list = actOrderMapper.selectByActId(activityId);
        return list;
    }

    /**
     * 根据活动id,查询用户信息,以及用户相册
     *
     * @param activityId 活动id
     * @return
     */
    @Override
    public ServiceResponse<Pager<UAlbum>> selectUAlbum(Integer activityId, Integer pageIndex) {
        List<UAlbum> list = actOrderMapper.selectUAlbum(activityId);
        return userService.foruAlbumList(list, pageIndex);
    }

    @Override
    public List<UAlbum> selectUAlbum(Integer activityId){
        List<UAlbum> list = actOrderMapper.selectUAlbum(activityId);
        return list;
    }

    /**
     * 活动单信息
     *
     * @param userId     用户id
     * @param activityId 活动id
     * @return
     */
    @Override
    public ServiceResponse<ActOrder> selectStatusByUAid(Integer userId, Integer activityId) {
        ActOrder actOrder = actOrderMapper.selectStatusByUAid(userId, activityId);
        if (actOrder != null) {
            return ServiceResponse.createBySuccess(actOrder);
        }
        return ServiceResponse.createByError("没有报名");
    }

    /**
     * 活动邀请
     *
     * @param movieInvitation 邀请实体类
     * @return 是否邀请成功
     */
    @Override
    public ServiceResponse<String> addInvitation(MovieInvitation movieInvitation) {
        ActOrder actOrder = actOrderMapper.selectStatusByUAid(movieInvitation.getMyid(), movieInvitation.getActivityId());
        User user = userService.selectByPrimaryKey(movieInvitation.getMyid());
        if(user.getUserBlacklist()==1){//查看用户是否是黑名单
            return ServiceResponse.createByError("用户处于黑名单内");
        }
        if (actOrder != null) {
            int getrows = movieInvitationMapper.insertSelective(movieInvitation);
            if (getrows > 0) {
                return ServiceResponse.createBySuccess("邀请成功,等待对方同意");
            }
            return ServiceResponse.createBySuccess("邀请失败");
        }
        return ServiceResponse.createByError("没有报名");
    }

    /**
     * 接受邀请
     * @param movieInvitation myid,youid
     * @return true false 0(已被邀请),1(同意失败),2(接受邀请成功)
     */
    @Override
    @Transactional
    public ServiceResponse<Integer> movieAgree(MovieInvitation movieInvitation) {
        List<MovieInvitation> moveList = movieInvitationMapper.selectStatusByMA(movieInvitation.getMyid(), movieInvitation.getActivityId());
        List<MovieInvitation> moveList2 = movieInvitationMapper.selectStatusByYA(movieInvitation.getMyid(), movieInvitation.getActivityId());
        for (MovieInvitation movieInvitation1 : moveList) {
            if (movieInvitation1.getStatus() == 2) {
                return ServiceResponse.createBySuccess(0);
            }
        }
        for (MovieInvitation movieInvitation2 : moveList2) {
            if (movieInvitation2.getStatus() == 2) {
                return ServiceResponse.createBySuccess(0);
            }
        }
        int getrows = movieInvitationMapper.updateByPrimaryKeySelective(movieInvitation);
        if (getrows > 0) {
            //修改双方得订单状态
            ActOrder actOrder = actOrderMapper.selectStatusByUAid(movieInvitation.getMyid(),movieInvitation.getActivityId());
            ActOrder actOrder2 = actOrderMapper.selectStatusByUAid(movieInvitation.getYouid(),movieInvitation.getActivityId());
             actOrder.setStatus(1);
            actOrder2.setStatus(1);
            int uprows = actOrderMapper.updateActOrder(actOrder);
            int uprows2 = actOrderMapper.updateActOrder(actOrder2);
            if((uprows+uprows2)==2){
                return ServiceResponse.createBySuccess(1);
            }
           return ServiceResponse.createByError();
        }
        return ServiceResponse.createByError();
    }

    /**
     * 拒绝邀请
     *
     * @param movieInvitation myid,youid
     * @return true false
     */
    @Override
    public ServiceResponse<String> movieRefuse(MovieInvitation movieInvitation) {
        int getrows = movieInvitationMapper.updateByPrimaryKeySelective(movieInvitation);
        if (getrows > 0) {
            return ServiceResponse.createBySuccess("拒绝邀请");
        }
        return ServiceResponse.createByError("拒绝失败");
    }

    /**
     * 邀约状态
     * @param myid       自己id
     * @param youid      被邀请id
     * @param activityId 活动id
     * @return msg[0(邀请中), 1(已拒绝), 2(已同意), 3(邀请), 4(同意和拒绝), 5(已被邀请)]
     */
    @Override
    public ServiceResponse<Integer> movieStatus(Integer myid, Integer youid, Integer activityId) {
        MovieInvitation movieInvitation = movieInvitationMapper.selectStatusByMYA(myid, youid, activityId);
        //判断有没进行邀请
        if (movieInvitation != null) {
            if (movieInvitation.getStatus() == 2) {
                return ServiceResponse.createBySuccess(2);
            } else if (movieInvitation.getStatus() == 1) {
                return ServiceResponse.createBySuccess(1);
            } else {
                //查询有没被别人邀请成功
                List<MovieInvitation> moveList = movieInvitationMapper.selectStatusByMA(youid, activityId);
                for (MovieInvitation movieInvitation1 : moveList) {
                    if (movieInvitation1.getStatus() == 2) {
                        return ServiceResponse.createBySuccess(5);
                    }
                }
                return ServiceResponse.createBySuccess(0);
            }
        } else {
            //别人邀请我的
            List<MovieInvitation> moveList = movieInvitationMapper.selectStatusByMA(youid, activityId);
            List<MovieInvitation> moveList2 = movieInvitationMapper.selectStatusByYA(youid, activityId);
            if (moveList != null) {
                //别人邀请我的状态
                MovieInvitation movieInvitation2 = movieInvitationMapper.selectStatusByMYA(youid, myid, activityId);
                //有没被别人邀请成功
                for (MovieInvitation movieInvitation1 : moveList) {
                    if (movieInvitation1.getStatus() == 2) {
                        if(movieInvitation1.getYouid()==myid){
                            return ServiceResponse.createBySuccess(2);
                        }
                        return ServiceResponse.createBySuccess(5);
                    }
                }
                for (MovieInvitation movieInvitation1 : moveList2) {
                    if (movieInvitation1.getStatus() == 2) {
                        if(movieInvitation1.getMyid()==myid){
                            return ServiceResponse.createBySuccess(2);
                        }
                        return ServiceResponse.createBySuccess(5);
                    }
                }
                if (movieInvitation2 != null) {
                    if (movieInvitation2.getStatus() == 2) {
                        return ServiceResponse.createBySuccess(2);
                    } else if (movieInvitation2.getStatus() == 1) {
                        return ServiceResponse.createBySuccess(1);
                    } else {
                        return ServiceResponse.createBySuccess(4);
                    }
                }
                return ServiceResponse.createBySuccess(3);

            } else {
                return ServiceResponse.createBySuccess(3);
            }
        }
    }

    /**
     * 查询邀请所有信息
     * @param activityId 活动id
     * @return true(List<MovieMYuser>) false
     */
    @Override
    public List<MovieMYuser> selectMovieMYuser(Integer activityId) {
        return movieInvitationMapper.selectMovieMYuser(activityId);
    }

    /**
     * 修改活动订单
     * @param actOrder 活动表
     * @return true false
     */
    @Override
    public ServiceResponse<String> updateManagerOrder(ActOrder actOrder) {
        int getrows = actOrderMapper.updatemanagerAct(actOrder);
        if(getrows>0){
            return ServiceResponse.createBySuccess("修改成功");
        }
        return ServiceResponse.createByError("修改失败!");
    }

    /**
     * 删除活动订单
     * @param id 活动id
     * @return true false
     */
    @Override
    public ServiceResponse<String> delectActOrder(Integer id) {
        int getrows = actOrderMapper.deleteActOrderById(id);
        if(getrows>0){
            return ServiceResponse.createBySuccess("修改成功");
        }
        return ServiceResponse.createByError("修改失败!");
    }

}

package com.example.peach.service.impl;

import com.example.peach.common.ServiceResponse;
import com.example.peach.dao.UserVipMapper;
import com.example.peach.pojo.UserVip;
import com.example.peach.pojo.merge.UvipUser;
import com.example.peach.service.UserVipService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class UserVipServiceImpl implements UserVipService {
    @Resource
    private UserVipMapper userVipMapper;
    private static final Logger L = Logger.getLogger(UserVipServiceImpl.class);
    /**
     * 修改用户会员信息
     * @param userVip
     * @return
     */
    @Override
    public ServiceResponse<String> updateByUserId(UserVip userVip) {
        int getrows = userVipMapper.updateByUserId(userVip);
        if(getrows>0){
            return ServiceResponse.createBySuccess("修改成功");
        }else{
            L.warn(userVip.getId()+"修改失败");
            return ServiceResponse.createByError("修改失败");
        }
    }

    /**
     * 添加用户vip信息
     * @param userId 用户id
     * @return
     */
    @Override
    public ServiceResponse<String> addUserVip(Integer userId) {
        UserVip userVip = new UserVip();
        userVip.setUserId(userId);
        int getrows = userVipMapper.insertSelective(userVip);
        if(getrows>0){
            return ServiceResponse.createBySuccess("用户vip信息,添加成功");
        }else{
            L.warn(userVip.getUserId()+"用户vip信息,添加失败");
            return ServiceResponse.createByError("用户vip信息,添加失败");
        }
    }

    /**
     * 约见成功相减
     * @param zUserid  主约id
     * @param bUserid   被约id
     * @return
     */
    @Transactional
    @Override
    public ServiceResponse<String> upateUappointmentByid(Integer zUserid, Integer bUserid) {
        //查询user约见次数
        UserVip zuserVip = userVipMapper.selectByUserId(zUserid);
        int Zappointment = 0;
        Zappointment= zuserVip.getVipAppointment();
        //判断用户
        if(Zappointment>0){
            Zappointment-=1;
            int getrows = userVipMapper.updateAppiontmentByUserId(Zappointment,zUserid);
            //减主约见次数
            if(getrows>0){
                //查询user约见次数
                UserVip buserVip = userVipMapper.selectByUserId(zUserid);
                int Bappointment = buserVip.getVipAppointment();
                Bappointment-=1;
                int getrows2 = userVipMapper.updateAppiontmentByUserId(Bappointment,bUserid);
                //减被约见次数
                if(getrows2>0){
                    return ServiceResponse.createBySuccess("约见相减成功");
                }else{
                    L.warn("用户"+bUserid+"约见次数相减失败");
                    return ServiceResponse.createByError("约见次数相减失败");
                }
            }else{
                L.warn("用户"+zUserid+"约见次数相减失败");
                return ServiceResponse.createByError("约见次数相减失败");
            }
        }else{
            L.warn("用户"+zUserid+"没有约见次数");
            return ServiceResponse.createByError("您没有约见次数");
        }
    }

    /**
     * 修改钱包押金
     * @param userVip  会员信息实体类
     * @return true false
     */
    @Override
    public ServiceResponse<String> updateUdepositByUserId(UserVip userVip) {
        Double money = null;
        if(userVip.getUserWallet()!=null) {
            money = userVip.getUserWallet();
            money += userVipMapper.selectByUserId(userVip.getUserId()).getUserWallet();
            userVip.setUserWallet(money);
        }else {
            money = userVip.getUserDeposit();
            money += userVipMapper.selectByUserId(userVip.getUserId()).getUserDeposit();
            userVip.setUserDeposit(money);
        }
        int getrows = userVipMapper.updateUMoneyByUserId(userVip);
        if (getrows>0){
            return ServiceResponse.createBySuccess("钱包修改成功");
        }else{
            return ServiceResponse.createByError("钱包修改失败");
        }
    }

    /**
     * 会员是否过期
     * @param userId
     * @return
     */
    @Override
    public ServiceResponse selectCreatAndEndByUserId(Integer userId) {
//        SimpleDateFormat myFmt = new SimpleDateFormat("yyMMddHHmmss");
       UvipUser uvipUser = userVipMapper.selectUvipUser(userId);
       if(uvipUser!=null) {
           if(uvipUser.getVipGrade()>0) {
               Date d1 = uvipUser.getVipEndtime();
               Date d2 = new Date();
               long hour = (d1.getTime() - d2.getTime()) / (1000 * 60 * 60);
               long day = (d1.getTime() - d2.getTime()) / (1000 * 60 * 60 * 24);
               if (hour > 0) {
                   return ServiceResponse.createBySuccess("会员过期还有" + day + "天",uvipUser);
               } else {
                   L.warn(userId+"会员已过期");
                   return ServiceResponse.createByError(2);//会员已过期
               }
           }else {
               L.warn("用户"+userId+"不是会员");
               return ServiceResponse.createByError(1);//您不是会员
           }
       }else{
           L.warn("没有"+userId+"用户的会员记录");
           return ServiceResponse.createByError("没有此用户的会员记录");//"没有此用户的会员记录"
       }
    }

    /**
     *  查询所有vip(非vip和vip)信息
     * @return
     */
    @Override
    public ServiceResponse<List> selectAllUvipUser() {
        List<UvipUser> list = userVipMapper.selectAllUvipUser();
        if(list!=null){
            return  ServiceResponse.createBySuccess(list);
        }else{
            return ServiceResponse.createByError("查询失败");
        }
    }

    /**
     *  查询所有vip信息
     * @return List<UvipUser>
     */
    @Override
    public List<UvipUser> selectAllvip(Integer over) {
        List<UvipUser> list = userVipMapper.selectAllUvipUser();
        List<UvipUser> uvipUserList = new ArrayList<UvipUser>();
        List<UvipUser> overUserList = new ArrayList<UvipUser>();
        if(list==null){
            return null;
        }else{
            if(over==0) {
                for (UvipUser uvipUser : list) {
                    if (uvipUser.getVipGrade() > 0) {
                        Date d1 = uvipUser.getVipEndtime();
                        Date d2 = new Date();
                        long hour = (d1.getTime() - d2.getTime()) / (1000 * 60 * 60);
                        if(hour>0){
                            uvipUserList.add(uvipUser);
                        }
                    }
                }
                return uvipUserList;
            }else if(over==1){
                for (UvipUser uvipUser : list) {
                    if (uvipUser.getVipGrade() > 0) {
                        Date d1 = uvipUser.getVipEndtime();
                        Date d2 = new Date();
                        long hour = (d1.getTime() - d2.getTime()) / (1000 * 60 * 60);
                        if(hour<=0){
                            overUserList.add(uvipUser);
                        }
                    }
                }
                return overUserList;
            }
        }
       return null;
    }

    /**
     * 查询用户信息
     * @param userId 用户id
     * @return
     */
    @Override
    public ServiceResponse<UvipUser> selectUvipUser(Integer userId) {
        UvipUser uvipUser = userVipMapper.selectUvipUser(userId);
        if(uvipUser!=null){
            return ServiceResponse.createBySuccess(uvipUser);
        }
        return ServiceResponse.createByError("没有用户vip信息");
    }

    /**
     * 根据openid查询用户信息
     * @param openid
     * @return
     */
    @Override
    public ServiceResponse<UvipUser> selectUvipUserByopenid(String openid) {
        UvipUser uvipUser = userVipMapper.selectUvipUserByopenid(openid);
        if(uvipUser!=null){
            return ServiceResponse.createBySuccess(uvipUser);
        }
        return ServiceResponse.createByError("没有用户vip信息");
    }

    /**
     * 动态修改UserVip
     * @param uvipUser
     * @return
     */
    @Override
    public ServiceResponse<String> updateUserVip(UvipUser uvipUser) {
        int getrows = userVipMapper.updateByPrimaryKeySelective(uvipUser);
        if(getrows>0){
            return ServiceResponse.createBySuccess();
        }
        return ServiceResponse.createBySuccess("userfree状态修改失败");
    }

    /**
     * 判断有没约见次数
     * @param userId
     * @return
     */
    @Override
    public ServiceResponse<String> selectAppointment(Integer userId) {
        UvipUser uvipUser = userVipMapper.selectUvipUser(userId);
        if(uvipUser!=null){
            if(uvipUser.getVipAppointment()>0){
                return ServiceResponse.createBySuccess();
            }
            return ServiceResponse.createByError("没有约见次数");
        }
        return ServiceResponse.createByError("没有用户vip信息");
    }

    /**
     * 根据id动态修改uservip信息
     * @param request uservip信息
     * @return true false
     * @throws ParseException 时间格式错误
     */
    @Override
    public ServiceResponse<String> updateUservipByid(HttpServletRequest request) throws ParseException {
        UserVip userVip = new UserVip();
        userVip.setId(Integer.valueOf(request.getParameter("id")));
        if(request.getParameter("vipGrade")!=null) {
            userVip.setVipGrade(Integer.valueOf(request.getParameter("vipGrade")));
        }
        if(request.getParameter("vipAppointment")!=null) {
            userVip.setVipAppointment(Integer.valueOf(request.getParameter("vipAppointment")));
        }
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        if(request.getParameter("vipCreatetime")!=null&&request.getParameter("vipEndtime")!=null) {
            userVip.setVipCreatetime(sdf.parse(request.getParameter("vipCreatetime")));
            userVip.setVipEndtime(sdf.parse(request.getParameter("vipEndtime")));
        }
        if(request.getParameter("userFree").equals("是")){
            userVip.setUserFree(true);
        }else{
            userVip.setUserFree(false);
        }
        int getrows = userVipMapper.updateByid(userVip);
        if(getrows>0){
            return ServiceResponse.createBySuccess("修改成功");
        }else{
            return ServiceResponse.createByError("修改失败!");
        }
    }

    /**
     * 删除用户vip信息
     * @param id 用户user_id
     * @return true  false
     */
    @Override
    public ServiceResponse<String> delectUvip(Integer id) {
        int getrows = userVipMapper.deleteByPrimaryKey(id);
        if(getrows>0){
            return ServiceResponse.createBySuccess();
        }
        return ServiceResponse.createByError();
    }
}

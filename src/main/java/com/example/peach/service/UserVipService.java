package com.example.peach.service;

import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.UserVip;
import com.example.peach.pojo.merge.UvipUser;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

public interface UserVipService {
    //修改用户信息
    ServiceResponse<String> updateByUserId(UserVip userVip);
    //添加用户vip信息
    ServiceResponse<String> addUserVip(Integer userId);
    //约见成功相减zUserid:主约id,bUserid:被约id
    ServiceResponse<String> upateUappointmentByid(Integer zUserid, Integer bUserid);
    //钱包充值
    ServiceResponse<String> updateUdepositByUserId(UserVip userVip);
    //是否会员或会员过期
    ServiceResponse<UvipUser> selectCreatAndEndByUserId(Integer userId);
    //查询所有vip(非vip和vip)信息
    ServiceResponse<List> selectAllUvipUser();
    //根据userId查询信息
    ServiceResponse<UvipUser> selectUvipUser(Integer userId);
    //根据openid查询信息
    ServiceResponse<UvipUser> selectUvipUserByopenid(String openid);
    //修改userVip
    ServiceResponse<String> updateUserVip(UvipUser uvipUser);
    //判断有没约见次数
    ServiceResponse<String> selectAppointment(Integer userId);
    //根据id动态修改uservip信息
    ServiceResponse<String> updateUservipByid(HttpServletRequest request) throws ParseException;
    //删除用户vip信息
    ServiceResponse<String> delectUvip(Integer id);
    //查询所有会员
    List<UvipUser> selectAllvip(Integer over);
}

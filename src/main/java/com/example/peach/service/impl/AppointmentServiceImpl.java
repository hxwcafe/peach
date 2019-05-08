package com.example.peach.service.impl;

import com.example.peach.common.Pager;
import com.example.peach.common.ServiceResponse;
import com.example.peach.dao.AlbumMapper;
import com.example.peach.dao.AppointmentMapper;
import com.example.peach.pojo.Album;
import com.example.peach.pojo.Appointment;
import com.example.peach.pojo.User;
import com.example.peach.pojo.merge.AppointMYuser;
import com.example.peach.pojo.merge.UAlbum;
import com.example.peach.service.AppointmentService;
import com.example.peach.service.UserService;
import com.example.peach.service.UserVipService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2018/11/21.
 */
@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Resource
    private AppointmentMapper appointmentMapper;
    @Resource
    private UserVipService userVipService;
    @Resource
    private UserService userService;
    @Resource
    private AlbumMapper albumMapper;
    //创建约见数据
    @Override
    public ServiceResponse<String> insertAppointment(Appointment appointment) {
        //判断是否是会员
//        ServiceResponse vipResponse = userVipService.selectCreatAndEndByUserId(appointment.getMyid());
//        if (vipResponse.isSuccess()) {
//            ServiceResponse uVAppointmentResponse = userVipService.selectAppointment(appointment.getMyid());
//            if(uVAppointmentResponse.isSuccess()) {
            User user = userService.selectByPrimaryKey(appointment.getMyid());
            if(user.getUserBlacklist()==1){//查看用户是否是黑名单
                return ServiceResponse.createByError("用户处于黑名单内");
            }
           if(userService.selectByPrimaryKey(appointment.getMyid()).getUserphone()!=null) {
               int rs = appointmentMapper.insertAppointment(appointment);
               if (rs > 0) {
                   return ServiceResponse.createBySuccess("约见成功！");
               } else {
                   return ServiceResponse.createByError("约见失败！");
               }
           }
           return ServiceResponse.createByError("没有注册");
//            }
//            return uVAppointmentResponse;
//        }
//        return vipResponse;
    }

    //查询自己的约见计划
    @Override
    public ServiceResponse<Pager<Appointment>> selectByMyId(Integer myid, Integer pageIndex) {
        List<Appointment> list=appointmentMapper.selectByMyId(myid);
        return this.forUAppointmentList(list,pageIndex);
    }

    //查询约见自己的计划
    @Override
    public ServiceResponse<Pager<Appointment>> selectByYouId(Integer youid, Integer pageIndex) {
        List<Appointment> list=appointmentMapper.selectByYouId(youid);
        return this.forUAppointmentList(list,pageIndex);
    }

    /**
     *  得到约见用户信息
     * @param list 约见用户信息
     * @param pageIndex 当前的页数
     * @return
     */
    public ServiceResponse<Pager<Appointment>> forUAppointmentList(List<Appointment> list, Integer pageIndex){
        if(list!=null && list.size()>0){
            for(Appointment appointment:list){
                for(UAlbum uAlbum:appointment.getList()){
                    List<Album> albumList = albumMapper.seletByuserId(uAlbum.getId());
                    uAlbum.setAlbumList(albumList);
                }
            }
            Pager<Appointment> pager = new Pager<>(pageIndex,10,list.size(),list);
            return ServiceResponse.createBySuccess(pager);
        }else{
            return ServiceResponse.createByError("没有约见计划！");
        }
    }

    //查询是否已有约见计划
    @Override
    public ServiceResponse<String> selectAppointment(Appointment appointment) {

        List<Appointment> list=appointmentMapper.selectAppointment(appointment);

        if(list!=null && list.size()>0){
            return ServiceResponse.createByError("已约见！");
        }else{
            return ServiceResponse.createBySuccess();
        }
    }

    //拒绝约见
    @Override
    public ServiceResponse<String> updateStatus1(Appointment appointment) {

        int rs=appointmentMapper.updateStatus1(appointment);
        if (rs>0){
            return ServiceResponse.createBySuccess();
        }else{
            return ServiceResponse.createByError("操作失败！");
        }
    }

    //同意约见
    @Override
    public ServiceResponse<String> updateStatus2(Appointment appointment) {

        int rs=appointmentMapper.updateStatus2(appointment);
        if (rs>0){
            return ServiceResponse.createBySuccess();
        }else{
            return ServiceResponse.createByError("操作失败！");
        }
    }

    /**
     * 所有约见信息显示
     * @return AppointMyuser 约见信息,约见人信息
     */
    @Override
    public List<AppointMYuser> selectAppointMYuser() {
       return appointmentMapper.selectAppointMYuser();
    }

    /**
     * 修改约见表
     * @param appointment 约见实体类
     * @return true false
     */
    @Override
    public ServiceResponse<String> upAppointment(Appointment appointment) {
        int getrows =  appointmentMapper.updateAppointment(appointment);
        if(getrows>0){
            return ServiceResponse.createBySuccess("修改成功");
        }
        return ServiceResponse.createByError("修改失败!");
    }


}

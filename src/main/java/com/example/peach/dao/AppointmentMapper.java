package com.example.peach.dao;

import com.example.peach.pojo.Appointment;
import com.example.peach.pojo.merge.AppointMYuser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

/**
 * Created by Administrator on 2018/11/21.
 */

@Component
@Mapper
public interface AppointmentMapper {

    //创建约见数据
    int insertAppointment(@ModelAttribute Appointment appointment);
    
    //查询自己的约见计划
    List<Appointment> selectByMyId(@Param("myid") Integer myid);

    //查询约见自己的计划
    List<Appointment> selectByYouId(@Param("youid") Integer youid);

    //查询是否已有约见计划
    List<Appointment> selectAppointment(@ModelAttribute Appointment appointment);

    //拒绝约见
    int updateStatus1(@ModelAttribute Appointment appointment);

    //同意约见
    int updateStatus2(@ModelAttribute Appointment appointment);
    //查询所有约见信息
    List<AppointMYuser> selectAppointMYuser();
    //修改约见表
    int updateAppointment(Appointment appointment);
}

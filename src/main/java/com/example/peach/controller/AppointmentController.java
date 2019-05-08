package com.example.peach.controller;

import com.example.peach.common.Pager;
import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.Appointment;
import com.example.peach.service.AppointmentService;
import com.example.peach.service.UserVipService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 约见
 * Created by Administrator on 2018/11/21.
 */
@RestController
@RequestMapping(value = "/appointment")
public class AppointmentController {

    @Resource
    private AppointmentService appointmentService;

    @Resource
    private UserVipService userVipService;

    //添加约见
    @RequestMapping(value = "/insertappointment", method = RequestMethod.POST)
    public ServiceResponse<String> insertAppointment(@ModelAttribute Appointment appointment) {
        if (appointment != null) {
            ServiceResponse<String> response = appointmentService.selectAppointment(appointment);
            if (!response.isSuccess()) {
                return response;
            } else {
                response = appointmentService.insertAppointment(appointment);
                return response;
            }
        } else {
            return ServiceResponse.createByError("传入空值！");
        }
    }

    //查询自己约见的对象
    @RequestMapping(value = "/selectbymyid", method = RequestMethod.GET)
    public ServiceResponse<Pager<Appointment>> selectByMyId(@RequestParam Integer userId, Integer pageIndex) {

        if (userId != null) {
            ServiceResponse<Pager<Appointment>> response = appointmentService.selectByMyId(userId,pageIndex);
            return response;
        } else {
            return ServiceResponse.createByError("传入空值!");
        }
    }


    //查询约见自己的对象
    @RequestMapping(value = "/selectbyyouid", method = RequestMethod.GET)
    public ServiceResponse<Pager<Appointment>> selectByYouId(@RequestParam Integer userId, Integer pageIndex) {

        if (userId != null) {
            ServiceResponse<Pager<Appointment>> response = appointmentService.selectByYouId(userId,pageIndex);
            return response;
        } else {
            return ServiceResponse.createByError("传入空值！");
        }
    }

    //拒绝约见
    @RequestMapping(value = "/updatestatus1", method = RequestMethod.POST)
    public ServiceResponse<String> updateStatus1(@ModelAttribute Appointment appointment) {
        if (appointment != null) {
            ServiceResponse<String> response = appointmentService.updateStatus1(appointment);
            if (!response.isSuccess()) {
                return response;
            } else {
                return ServiceResponse.createBySuccess("拒绝约见！");
            }
        } else {
            return ServiceResponse.createByError("传入空值！");
        }
    }

    //同意约见
    @RequestMapping(value = "/updatestatus2", method = RequestMethod.POST)
    public ServiceResponse<String> updateStatus2(@ModelAttribute Appointment appointment) {

        if (appointment != null) {
            ServiceResponse<String> response = appointmentService.updateStatus2(appointment);
            if (!response.isSuccess()) {
                return response;
            } else {
                //同意约见，修改约见次数
//                response = userVipService.upateUappointmentByid(appointment.getMyid(), appointment.getYouid());
//                if (!response.isSuccess()) {
//                    return ServiceResponse.createByError("约见次数不足！");
//                } else {
                    return ServiceResponse.createBySuccess("接受约见！");
//                }
            }
        } else {
            return ServiceResponse.createByError("传入空值！");
        }
    }
}

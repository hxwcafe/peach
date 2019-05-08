package com.example.peach.controllermanager;

import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.Appointment;
import com.example.peach.pojo.merge.AppointMYuser;
import com.example.peach.pojo.merge.UvipUser;
import com.example.peach.service.AdminService;
import com.example.peach.service.AppointmentService;
import com.example.peach.service.UserVipService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/manager")
public class UserVipManagerController {
    @Resource
    private UserVipService userVipService;
    @Resource
    private AppointmentService appointmentService;
    @Resource
    private AdminService adminService;
    /**
     * 查询所有用户会员非会员的信息
     * @return List<UvipUser>
     */
    @RequestMapping(value = "/all/vipinfo",method = RequestMethod.GET)
    public List<UvipUser> FindAllUserVip(Integer over){
        return userVipService.selectAllvip(over);
    }
    /**
     * 所有约见信息显示
     * @return AppointMyuser 约见信息,约见人信息
     */
    @RequestMapping(value = "/all/apponintment",method = RequestMethod.GET)
    public List<AppointMYuser> allAppointment(){
        return appointmentService.selectAppointMYuser();
    }

    /**
     * 修改约见表
     * @param appointment 约见表
     * @return true false
     */
    @RequestMapping(value = "/upapponintment",method = RequestMethod.POST)
    public ServiceResponse<String> upAppointment(Appointment appointment, HttpServletRequest request){
        ServiceResponse<String> lognResponse = adminService.judgelognAdmin(request);
        if(!lognResponse.isSuccess()) {
            return lognResponse;
        }
        return appointmentService.upAppointment(appointment);
    }

}

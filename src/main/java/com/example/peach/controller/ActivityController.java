package com.example.peach.controller;

import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.Activity;
import com.example.peach.service.ActivityService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 活动
 * Created by Administrator on 2018/11/15.
 */
@RestController
@RequestMapping("/activity")
public class ActivityController {
    @Resource
    private ActivityService activityService;

    /**
     * 根据id查询Activity
     * @param id
     * @return
     */
    @RequestMapping(value = "/getactivity",method = RequestMethod.GET)
    public ServiceResponse<Map> getActivity(@RequestParam Integer id){
      return   activityService.selectById(id);
    }

    /**
     * 查询所有活动
     */
    @RequestMapping(value="/selectactivity",method = RequestMethod.GET)
    public ServiceResponse<Map<String,List<Activity>>> selectActivity(){
        ServiceResponse<Map<String,List<Activity>>> mapServiceResponse=  activityService.selectActivity();
         return mapServiceResponse;
    }


}

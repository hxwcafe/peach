package com.example.peach.controller;

import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.Mate;
import com.example.peach.pojo.merge.UAMate;
import com.example.peach.service.MateService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/11/22.
 */
@RestController
@RequestMapping("/mate")
public class MateController {
    @Resource
    private MateService mateService;

    /**
     * 根据userId查询
     */
    @RequestMapping(value = "/selectByuserId", method = RequestMethod.GET)
    public Map<String, Object> selectByuserId(Integer userId) {
        Mate mate = mateService.selectByuserId(userId);
        Map<String, Object> map = new HashMap<>();
        map.put("mate",mate);
        return  map;
    }
    /**
     * 添加Mate
     */
    @RequestMapping(value = "/insertMate",method = RequestMethod.POST)
    public ServiceResponse<String> insertMate(@ModelAttribute Mate mate){
        return mateService.insertMate(mate);
    }
    /**
     * 根据userId修改
     */
    @RequestMapping(value = "/updateMate",method = RequestMethod.POST)
    public ServiceResponse<String> updateMate(@ModelAttribute Mate mate){
        return mateService.updateMate(mate);
    }
    /**
     * 根据openId查询
     */
    @RequestMapping(value = "/selectByOpenId",method = RequestMethod.GET)
    public Map<String, Object> selectByOpenId(@RequestParam String  openid){
        Mate mates=mateService.selectByOpenId(openid);
        Map<String, Object> map = new HashMap<>();
        map.put("mate",mates);
        return map;
    }

    /**
     * 根据userid,获取Alum,Mate,Uservip,User
     * @param userId 用户id
     * @return User,UserVip,Alum,Mate
     */
    @RequestMapping(value = "/selectuamate",method = RequestMethod.GET)
    public ServiceResponse<UAMate> selectUAMate(Integer userId){
       return mateService.selectUAMate(userId);
    }
}

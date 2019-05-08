package com.example.peach.controller;

import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.merge.UvipUser;
import com.example.peach.service.UserVipService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController

public class UserVipController {
    @Resource
    private UserVipService userVipService;

    /**
     * 获取用户vip信息
     * @param userId
     * @return
     */
    @RequestMapping(value = "vipoverdue",method = RequestMethod.GET)
    public ServiceResponse<UvipUser> selectCreatAndEndByUserId(Integer userId){
      ServiceResponse<UvipUser> useResponse= userVipService.selectCreatAndEndByUserId(userId);
      return useResponse;
    }
}

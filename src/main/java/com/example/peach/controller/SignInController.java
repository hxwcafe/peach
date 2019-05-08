package com.example.peach.controller;

import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.SignIn;
import com.example.peach.service.SignInService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by Administrator on 2018/12/6.
 */
@RestController
@RequestMapping("/signIn")
public class SignInController {

    @Resource
    private SignInService signInService;

    /**
     * 查询是否签到
     * @param userId
     * @return
     */
    @RequestMapping(value = "/selectByuserId",method = RequestMethod.GET)
    public ServiceResponse<Map> selectByuserId(@RequestParam Integer userId){
        return signInService.selectByuserId(userId);
    }

    /**
     * 签到
     * @param signIn
     * @return
     */
    @RequestMapping(value = "/insertSignIn",method = RequestMethod.POST)
    public ServiceResponse<Map> insertSignIn(@ModelAttribute SignIn signIn) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp  dates =Timestamp.valueOf(format.format(new Date()));
        signIn.setSignInTime(dates);
        return signInService.insertSignIn(signIn);
    }
}

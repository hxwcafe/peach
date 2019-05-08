package com.example.peach.controller;

import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.User;
import com.example.peach.service.UserService;
import com.example.peach.util.AesCbcUtil;
import com.example.peach.util.SmsVerification;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 短信验证
 * Created by Administrator on 2018/12/7.
 */
@RestController
@RequestMapping(value = "/send")
public class SendSmsController {

    @Resource
    private SmsVerification smsVerification;
    @Resource
    private UserService userService;

    @RequestMapping(value = "/sms", method = RequestMethod.POST)
    public ServiceResponse sendSms(@RequestParam String phoneNumber) {

        ServiceResponse response=userService.selectPhone(phoneNumber);
        if (!response.isSuccess()){
            return response;
        }else{
            return smsVerification.Send(phoneNumber);
        }
    }


    @RequestMapping(value = "/verification", method = RequestMethod.POST)
    public ServiceResponse SmsVerification(@ModelAttribute User user,@RequestParam String number) {
        return smsVerification.Verification(user,number);
    }

}

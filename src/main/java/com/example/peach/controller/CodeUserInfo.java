package com.example.peach.controller;


import com.example.peach.common.Conts;
import com.example.peach.common.ServiceResponse;
import com.example.peach.configuration.Configure;
import com.example.peach.pojo.User;
import com.example.peach.pojo.merge.UvipUser;
import com.example.peach.service.AesCbc;
import com.example.peach.service.UserService;
import com.example.peach.service.UserVipService;
import com.example.peach.util.*;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 登录授权
 * Created by Administrator on 2018/11/15.
 */
@RestController
public class CodeUserInfo {
    @Resource
    private UserService userService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private UserVipService userVipService;

    /**
     * 微信授权后修改信息
     *
     * @param user openid  nickName avatarUrl gender country province city language
     * @return
     */
    @RequestMapping(value = "/decodeUserInfo", method = RequestMethod.POST)
    public ServiceResponse<String> decodeUserInfo(@ModelAttribute User user) {
        ServiceResponse<String> response = userService.lognUser(user);
        return response;
    }


    /**
     * 获取openid
     *
     * @param code
     * @return true false 0 code为空,1 code无效
     */
    @RequestMapping(value = "/decodeOpenid", method = RequestMethod.GET)
    public ServiceResponse decodeOpenid(String code) {
        if (code == null || code.length() == 0) {
            return ServiceResponse.createByError(0);
        }
        JSONObject json = AesCbcUtil.ResponseopenidSessionKey(code);
        User user = new User();
        String openid = (String) json.get("openid");
        String session_key = (String) json.get("session_key");
        //缓存session_key
       Boolean bool =  redisUtil.set(openid,session_key,-1);
        if (!bool) {
            return ServiceResponse.createByError("session_key缓存失败");
        }
        if (openid == null) {
            return ServiceResponse.createByError(1);
        }
        user.setOpenid(openid);
        ServiceResponse<Map> response = userService.selectByOpenid(user.getOpenid(), Conts.OPENID);
        if (response.isSuccess()) {
            ServiceResponse<UvipUser> uvipUserResponse = userVipService.selectUvipUserByopenid(user.getOpenid());
            return uvipUserResponse;
        }else {
            return ServiceResponse.createByError(user);
        }
    }

    /**
     * 更新手机号
     *
     * @return
     */
    private static final String WATERMARK = "watermark";
    private static final String APPID = "appid";
    @Resource
    private AesCbc aesCbc;

    @RequestMapping(value = "/deupdatephone", method = RequestMethod.POST)
    public ServiceResponse updatePhone(String encryptedData, String iv, String openid){
        String result = "";
        try {
            String session_key = (String) redisUtil.get(openid);
            System.out.println("openid======"+openid);
            System.out.println("session_key======"+session_key);
            User user = new User();
//            if(session_key==null){
//                return ServiceResponse.createByError("session_key没有取到");
//            }
            if (openid == null) {
                return ServiceResponse.createByError("code无效");
            }
            user.setOpenid(openid);
            ServiceResponse pdOpenid = userService.selectByOpenid(openid, "OPENID");
            if (!pdOpenid.isSuccess()) {
                byte[] resultByte = aesCbc.decrypt(Base64.decodeBase64(encryptedData), Base64.decodeBase64(session_key), Base64.decodeBase64(iv));
                if (null != resultByte && resultByte.length > 0) {
                    result = new String(WxPKCS7Encoder.decode(resultByte), "UTF-8");
                    JSONObject jsonObject = JSONObject.fromObject(result);
                    String decryptAppid = jsonObject.getJSONObject(WATERMARK).getString(APPID);
                    if (!Configure.getAppID().equals(decryptAppid)) {
                        result = "";
                    }
                    System.out.println(jsonObject);
                    String phoneNumber = (String) jsonObject.get("phoneNumber");
                    System.out.println("phoneNumber=========="+phoneNumber);
                    user.setUserphone(phoneNumber);
                } else {
                    return ServiceResponse.createByError("解密失败");
                }
            }
            ServiceResponse<UvipUser> uvipUserServiceResponse = userService.addOpenid(user);
            return uvipUserServiceResponse;
//            ServiceResponse<UvipUser> uvipUserServiceResponse = userService.lognUpdateUserPhonse(user);
//            return uvipUserServiceResponse;
        } catch (Exception e) {
            result = "";
            e.printStackTrace();
            return ServiceResponse.createByError("解密失败");
        }
    }

}
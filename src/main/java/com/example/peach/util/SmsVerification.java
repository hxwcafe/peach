package com.example.peach.util;


import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.User;
import com.example.peach.pojo.merge.UvipUser;
import com.example.peach.service.UserService;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.json.JSONException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Random;


/**
 * 短信验证工具类
 * Created by Administrator on 2018/12/7.
 */

@Service
public class SmsVerification {

    @Resource
    private UserService userService;

    @Resource
    private RedisUtil redisUtil;

    // 短信应用SDK AppID
    static final int appid = 1400167937; // 1400开头

    // 短信应用SDK AppKey
    static final String appkey = "e5464a6d2fc4e2ffd236437549d9bfaa";

    // 需要发送短信的手机号码
    //static final String[] phoneNumbers = {"21212313123", "12345678902", "12345678903"};

    // 短信模板ID，需要在短信应用中申请
    static final int templateId = 242757; // NOTE: 这里的模板ID`7839`只是一个示例，真实的模板ID需要在短信控制台中申请
    //templateId7839对应的内容是"您的验证码是: {1}"
    // 签名
    static final String smsSign = "他她派"; // NOTE: 这里的签名"腾讯云"只是一个示例，真实的签名需要在短信控制台中申请，另外签名参数使用的是`签名内容`，而不是`签名ID`

    public ServiceResponse Send(String phoneNumber) {

        String number = getMsgCode();

        if (!redisUtil.hasKey(phoneNumber) ){
            Boolean bool = redisUtil.set(number, number, 240);
            Boolean lean = redisUtil.set(phoneNumber, phoneNumber, 240);
            if (bool && lean) {
                try {
                    String[] params = {number, "4"};//数组具体的元素个数和模板中变量个数必须一致，例如事例中templateId:5678对应一个变量，参数数组中元素个数也必须是一个
                    SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
                    SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNumber, templateId, params, smsSign,"","");  // 签名参数未提供或者为空时，会使用默认签名发送短信
                    //System.out.println(result);
                    if (result.errMsg.equals("OK")) {
                        return ServiceResponse.createBySuccess("发送成功！");
                    } else {
                        return ServiceResponse.createByError("发送失败！");
                    }
                } catch (HTTPException e) {
                    // HTTP响应码错误
                    e.printStackTrace();
                    return ServiceResponse.createByError("HTTP响应码错误！");
                } catch (JSONException e) {
                    // json解析错误
                    e.printStackTrace();
                    return ServiceResponse.createByError("json解析错误！");
                } catch (IOException e) {
                    // 网络IO错误
                    e.printStackTrace();
                    return ServiceResponse.createByError("网络IO错误！");
                }
            } else {
                return ServiceResponse.createByError("请重新发送！");
            }
        } else {
            return ServiceResponse.createByError("请稍后发送！");
        }
    }


    public ServiceResponse Verification(User user, String number) {

        if (redisUtil.hasKey(number)) {
            Object num =redisUtil.get(number);
            Object phone =redisUtil.get(user.getUserphone());
            if (user.getUserphone().equals(phone) && number.equals(num)) {
//                int rs = userService.updateUserPhone(user);
////                if (rs > 0) {
////                    return ServiceResponse.createBySuccess("绑定成功！");
////                } else {
////                    return ServiceResponse.createByError("绑定失败！");
////                }
                ServiceResponse<UvipUser> uvipUserServiceResponse = userService.addOpenid(user);
                return uvipUserServiceResponse;
            } else {
                return ServiceResponse.createByError("输入错误！");
            }
        } else {
            return ServiceResponse.createByError("请发送验证码！");
        }
    }


    /**
     * 生成随机的6位数，短信验证码
     *
     * @return
     */
    private static String getMsgCode() {
        int n = 6;
        StringBuilder code = new StringBuilder();
        Random ran = new Random();
        for (int i = 0; i < n; i++) {
            code.append(Integer.valueOf(ran.nextInt(10)).toString());
        }
        return code.toString();
    }


}

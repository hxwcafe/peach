package com.example.peach.controller;

import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.pay.TemplateData;
import com.example.peach.service.PublicService;
import com.example.peach.service.TokenService;
import com.example.peach.util.Check;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

//公用控制器(放一些比较杂的接口)
@RestController
@RequestMapping(value = "/public")
public class PublicController {

    @Resource
    private PublicService publicService;
    @Resource
    private TokenService tokenService;

    @RequestMapping(value = "/recordExtension",method = RequestMethod.POST)
    //二维码分享,记录分享者与被分享者
    public ServiceResponse<Void> recordExtension(HttpServletRequest request){
        Check check = new Check();
        String uid = request.getParameter("uid");
        String eid = request.getParameter("eid");
//        try{
//            check.checkToken(request);
//        }catch(Exception e){
//            return ServiceResponse.createByError(e.getMessage());
//        }
        ServiceResponse<Void> response = null;

        try{
            response = publicService.recordExtension(  check.StringParseInteger(eid),check.StringParseInteger(uid));
        }catch(Exception e){
            return ServiceResponse.createByError(e.getMessage());
        }
        return response;
    }


    @RequestMapping(value = "/",method = RequestMethod.POST)
    public ServiceResponse<String> WXuserinfo(String openid, String templateid, String formid, String page){
        Map<String, TemplateData> m = new HashMap<>(5);

        //keyword1：订单类型，keyword2：下单金额，keyword3：配送地址，keyword4：取件地址，keyword5备注
        TemplateData keyword1 = new TemplateData();
        keyword1.setValue("新下单待抢单");
        m.put("keyword1", keyword1);

        TemplateData keyword2 = new TemplateData();
        keyword2.setValue("这里填下单金额的值");
        m.put("keyword2", keyword2);

        TemplateData keyword3 = new TemplateData();
        keyword3.setValue("这里填配送地址");
        m.put("keyword3", keyword3);

        TemplateData keyword4 = new TemplateData();
        keyword4.setValue("这里填取件地址");
        m.put("keyword4", keyword4);

        TemplateData keyword5 = new TemplateData();
        keyword5.setValue("这里填备注");
        m.put("keyword5", keyword5);
        return publicService.wxtsUserInfo(openid,templateid,formid,tokenService.selectById().getAccess_token(),page,m);
    }
}

package com.example.peach.util;


import com.alibaba.fastjson.JSONObject;
import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.OrderRefund;
import com.example.peach.pojo.Orderpay;
import com.example.peach.pojo.Token;
import com.example.peach.service.OrderRefundService;
import com.example.peach.service.TokenService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * 生成token工具类
 * Created by Administrator on 2018/11/16.
 */
@EnableScheduling // 此注解必加,必须要加，重中之重
@Component // 此注解必加
public class QuartzGetToken {

    @Resource
    TokenService tokenService;
    @Resource
    OrderRefundService orderRefundService;

    @Scheduled(cron = "0 0 0/1 * * ? ") // 每一小时执行一次
    public void goWork() throws Exception {

        System.out.println("每一小时执行一次的定时任务：" + new Date());
        System.out.println("执行获取Token：" + new Date());
        System.out.println("定时任务启动了");
        // 小程序唯一标识 (在微信小程序管理后台获取)
        String wxspAppid = "wxd472817c47fa123a";
        // 小程序的 app secret (在微信小程序管理后台获取)
        String wxspSecret = "9108525c9fcaa6a42fb79fb8f79270b2";
        //这里直接写死就可以，不用改，用法可以去看api
        String grant_type = "client_credential";
        //封装请求数据
        String params = "grant_type=" + grant_type + "&secret=" + wxspSecret + "&appid=" + wxspAppid;
        //发送GET请求
        String sendGet = HttpRequest.sendGet("https://api.weixin.qq.com/cgi-bin/token", params);

        // 解析相应内容（转换成json对象）
        JSONObject json = JSONObject.parseObject(sendGet);

        //拿到accesstoken
        String access_token = (String) json.get("access_token");
        //打印access_token
        //System.out.println(access_token);

        //下面的操作我是存放到数据库了，大家可以更具自己的业务进行存储
        //封装数据
        Token token = new Token();
        token.setId(1);
        token.setAccess_token(access_token);

        if(access_token!=null){

            //存储数据库，响应结果
            int rs = tokenService.updateById(access_token);
            if (rs > 0) {
                System.out.println("access_token更新成功");
            } else {
                System.out.println("access_token更新失败");
            }
        }else{
            //System.out.println("access_token获取为null");

        }
        //结束
        System.out.println("定时任务结束了");
    }

    @Scheduled(cron = "0 0 0/2 * * ? ") // 每一小时执行一次
    public void goRefund() {
        System.out.println("检查待退款订单:");
        //自动退款处理
        orderRefundService.automaticRefund();
    }


}

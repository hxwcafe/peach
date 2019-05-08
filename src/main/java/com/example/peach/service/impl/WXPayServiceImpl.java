package com.example.peach.service.impl;

import com.example.peach.common.ServiceResponse;
import com.example.peach.configuration.Configure;
import com.example.peach.dao.CommodityMapper;
import com.example.peach.pojo.*;
import com.example.peach.pojo.merge.UvipUser;
import com.example.peach.pojo.pay.OrderCloseInfo;
import com.example.peach.pojo.pay.OrderReturnInfo;
import com.example.peach.service.*;
import com.example.peach.util.*;
import com.thoughtworks.xstream.XStream;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WXPayServiceImpl implements WXPayService {
    @Resource
    private UserService userService;
    @Resource
    private OrderPayService orderPayService;
    @Resource
    private CommodityMapper commodityMapper;
    @Resource
    private UserVipService userVipService;
    @Resource
    private OrderActivityService orderActivityService;
    private static final Logger L = Logger.getLogger(WXPayServiceImpl.class);

    /**
     * 付款金额处理
     * @param openid 用户id
     * @param activity 活动类
     * @return map : money,body
     */
    @Override
    public Map actualMoney(String openid, Activity activity) {
        Map map = new HashMap();
        Double money = null;
        ServiceResponse<UvipUser> uvipUserServiceResponse = userVipService.selectUvipUserByopenid(openid);
        UvipUser uvipUser = uvipUserServiceResponse.getData();
        if(uvipUser.getUser().getGender()==1){
            money=activity.getActdeposit()+activity.getActcost()-uvipUser.getUserDeposit();
        }else if(uvipUser.getUser().getGender()==2){
            money=activity.getActdeposit()-uvipUser.getUserDeposit();
        }else{
            money=activity.getActdeposit()+activity.getActcost()-uvipUser.getUserDeposit();
        }
        map.put("money", money);
        map.put("deposit_fee",activity.getActdeposit()-uvipUser.getUserDeposit());
        map.put("openid",openid);
        map.put("activity_id", activity.getId());
        map.put("body","您参与"+activity.getActname()+":"+activity.getActcontentname()+",共支付"+money+"元,包含"+activity.getActcontentname()+
                ":"+activity.getActcost()+"元,押金:"+activity.getActdeposit()+"元,补交押金:"+(activity.getActdeposit()-uvipUser.getUserDeposit()));
        return map;
    }

    /**
     * 付款
     *
     * @param mapinfo
     * @return
     * @throws IllegalAccessException
     */
    @Transactional
    @Override
    public ServiceResponse<Map> paymettwe(Map mapinfo, HttpServletRequest request) throws IllegalAccessException{
        //处理小数点问题
        Double money = (Double) mapinfo.get("money");
//        Double money = (Double)0.01;
//        BigDecimal bg = new BigDecimal(money * 100);
//        double doubleValue = bg.setScale(bg,BigDecimal.ROUND_HALF_UP).doubleValue();
        long money2 = Math.round(money * 100);
        String amount = String.valueOf(money2);
        String openid = (String) mapinfo.get("openid");
        String body = (String) mapinfo.get("body");
        Map<String, String> paraMap = new TreeMap<String, String>();//自动遵循ASCII码大小
        //设置请求参数(小程序ID)
        paraMap.put("appid", Configure.getAppID());
        //设置请求参数(商品描述)
        paraMap.put("body", body);
        //设置请求参数(商户号)
        paraMap.put("mch_id", Configure.getMch_id());
        //设置请求参数(随机字符串)
        paraMap.put("nonce_str", RandomStringGenerator.getRandomStringByLength(32));
        //设置请求参数(商户订单号)
        paraMap.put("out_trade_no", RandomStringGenerator.getRandomStringtime(32));
        //设置请求参数(总金额)
        paraMap.put("total_fee", amount);
        //设置请求参数(终端IP)
        paraMap.put("spbill_create_ip", Configure.getSpbill_create_ip());
//        paraMap.put("spbill_create_ip",PayUtil.getIpAddress(request));
        //设置请求参数(通知地址)
        paraMap.put("notify_url", "http://localhost:8080/api/pay/weixin/WXpayBack");
//        paraMap.put("notify_url","http://www.thy66.cn/peach/api/pay/weixin/WXpayBack");
        //设置请求参数(交易类型)
        paraMap.put("trade_type", "JSAPI");
        //设置交易起始时间
        SimpleDateFormat myFmt = new SimpleDateFormat("yyyyMMddHHmmss");
        paraMap.put("time_start", myFmt.format(new Date()));
        //设置交易结束时间
        paraMap.put("time_expire", myFmt.format((new Date()).getTime() + 30 * 60 * 1000));
        paraMap.put("openid", openid);
        System.out.println("openid+++++" + paraMap.put("openid", openid));
        paraMap = PayUtil.paraFilter(paraMap);
        String stringA = Signature.formatUrlMap(paraMap, false, false);
        System.out.println("签名" + stringA);
        String sign = MD5.MD5Encode(stringA + "&key=" + Configure.getKey()).toUpperCase();
        paraMap.put("sign", sign);
        String sxml = PayUtil.MaptoXml(paraMap);
        Map map = new HashMap();
        try {
            String result = PayRequest.httpRequest(Configure.TYURL, "POST", sxml);
            System.out.println(result);
            XStream xStream = new XStream();
            xStream.alias("xml", OrderReturnInfo.class);
            OrderReturnInfo orderInfo = (OrderReturnInfo) xStream.fromXML(result);
            //应该创建 支付表数据
            if ("SUCCESS".equals(orderInfo.getReturn_code()) && orderInfo.getResult_code().equals(orderInfo.getReturn_code())) {
                long time = System.currentTimeMillis() / 1000;
                Map payInfo = new HashMap();
                payInfo.put("timeStamp", String.valueOf(time));
                payInfo.put("nonceStr", paraMap.get("nonce_str"));
                payInfo.put("package", "prepay_id=" + orderInfo.getPrepay_id());
                payInfo.put("signType", "MD5");
                String stringSignTemp = "appId=" + Configure.getAppID() + "&nonceStr=" + paraMap.get("nonce_str") + "&package=prepay_id=" + orderInfo.getPrepay_id() + "&signType=MD5&timeStamp=" + String.valueOf(time);
                //生成二次签名
                String paySign = PayUtil.sign(stringSignTemp, Configure.getKey(), "utf-8").toUpperCase();
                map.put("status", 200);
                map.put("msg", "统一下单成功!");
                map.put("data", payInfo);
                payInfo.put("paySign", paySign);
                //逻辑层  添加订单表
                User user = userService.selectByOpenid(openid);
                if (user == null) {
                    return ServiceResponse.createByError("此用户不存在", map);
                }
                //会员支付(暂时下单)
                if (mapinfo.get("activity_id")==null) {
                    Orderpay orderpay = new Orderpay();
                    orderpay.setOutTradeNo(paraMap.get("out_trade_no"));
                    orderpay.setUserId(user.getId());
                    orderpay.setTradeType(orderInfo.getTrade_type());
                    orderpay.setBody(body);
                    orderpay.setTotalFee(money);
                    orderpay.setTradeStatus("NOTPAY");//NOTPAY未支付
                    orderpay.setCommodityId((Integer) mapinfo.get("commodityId"));
                    orderpay.setCreateTime(paraMap.get("time_start"));
                    orderpay.setUpdateTime(paraMap.get("time_expire"));
                    int getrows = orderPayService.insertOrderPay(orderpay);
                    if (getrows > 0) {
                        System.out.println("用户订单添加成功");
                        orderClose(paraMap.get("out_trade_no"));
                        return ServiceResponse.createBySuccess(map);
                    } else {
                        System.out.println("用户订单导入失败");
                        L.warn(paraMap.get("out_trade_no") + "用户订单导入失败");
                        return ServiceResponse.createByError("用户订单导入失败", map);
                    }
                } else {
                    OrderActivity orderActivity = new OrderActivity();
                    orderActivity.setOutTradeNo(paraMap.get("out_trade_no"));
                    orderActivity.setUserId(user.getId());
                    orderActivity.setTradeType(orderInfo.getTrade_type());
                    orderActivity.setBody(body);
                    orderActivity.setTotalFee(money);
                    orderActivity.setDepositFee((Double) mapinfo.get("deposit_fee"));
                    orderActivity.setTradeStatus("NOTPAY");//NOTPAY未支付
                    orderActivity.setActivityId((Integer) mapinfo.get("activity_id"));
                    orderActivity.setCreateTime(myFmt.parse(paraMap.get("time_start")));
                    orderActivity.setUpdateTime(myFmt.parse(paraMap.get("time_expire")));
                    ServiceResponse orederAResponse = orderActivityService.insertOrderActivity(orderActivity);
                    if (orederAResponse .isSuccess()) {
                        System.out.println("活动订单添加成功");
                        orderClose(paraMap.get("out_trade_no"));
                        return ServiceResponse.createBySuccess(map);
                    } else {
                        System.out.println("活动订单导入失败");
                        L.warn(paraMap.get("out_trade_no") + "用户订单导入失败");
                        return ServiceResponse.createByError("用户订单导入失败", map);
                    }
                }
            }
            map.put("status", 500);
            map.put("msg", "统一下单失败!");
            map.put("data", null);
            return ServiceResponse.createByError(map);
            //将 数据包ID 返回
        } catch (Exception e) {
            L.error("微信 统一下单 异常：" + e.getMessage());
            e.printStackTrace();
        }
        L.error("微信 统一下单 失败");
        return ServiceResponse.createByError(null);
    }

    /**
     * 支付回调,修改user信息(vip,appointment,Integral)
     *
     * @param openid
     * @param out_trade_no
     * @param cash_fee
     */
    @Transactional
    public void wxpayBackmysql(String openid, String out_trade_no, Double cash_fee) throws ParseException {
        Orderpay orderpay = orderPayService.selectByouttradeno(out_trade_no);
        if(orderpay==null){
            ServiceResponse orderActivityResponse =orderActivityService.updatePayBack(out_trade_no);
            if(orderActivityResponse.isSuccess()){
                System.out.println("活动单修改成功");
            }else{
                System.out.println("活动单修改失败");
            }
        }else {
            User user = userService.selectByOpenid(openid);
            int id = user.getId();
            if (id == orderpay.getUserId()) {
                SimpleDateFormat myFmt = new SimpleDateFormat("yyyyMMddHHmmss");
                String time = myFmt.format(new Date());
                Orderpay updateOrderpay = new Orderpay();
                updateOrderpay.setTradeStatus("SUCCESS");
                updateOrderpay.setEndTime(time);
                updateOrderpay.setOutTradeNo(out_trade_no);
                int getrows = orderPayService.updateOrder_statusByout_trade_no(updateOrderpay);
                if (getrows > 0) {
                    System.out.println("订单记录修改成功");
                } else {
                    System.out.println("订单记录修改失败");
                }
                //判断是否充值的是会员
                if (orderpay.getCommodityId() <= 4 && orderpay.getCommodityId() >= 1) {
                    int userIntegral = (int) Math.round(cash_fee + user.getUserIntegral());//积分
                    int appointment = commodityMapper.selectByPrimaryKey(orderpay.getCommodityId()).getCommodityFrequency();
                    User updateuser = new User();
                    updateuser.setUserNewold(false);
                    updateuser.setUserIntegral(userIntegral);
                    updateuser.setOpenid(openid);
                    ServiceResponse updateUser = userService.updateUnewoldAndUIntegralByOpenid(updateuser);
                    if (updateUser.isSuccess()) {
                        System.out.println("用户积分,新老用户,状态修改成功");

                    } else {
                        System.out.println("用户积分,新老用户,修改失败");
                    }
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date());
                    cal.add(Calendar.YEAR, 1);//增加一年
                    UserVip userVip = new UserVip();
                    userVip.setUserId(orderpay.getUserId());
                    userVip.setVipGrade(orderpay.getCommodityId());
                    userVip.setVipAppointment(appointment);
                    userVip.setVipCreatetime(myFmt.parse(time));
                    userVip.setVipEndtime(cal.getTime());
                    ServiceResponse updateUserVip = userVipService.updateByUserId(userVip);
                    if (updateUserVip.isSuccess()) {
                        System.out.println("用户vip信息,修改成功");
                    } else {
                        System.out.println("用户vip信息,修改失败");
                    }
                }
                //充值的钱包
                else {
                    UserVip userVip = new UserVip();
                    userVip.setUserId(orderpay.getUserId());
                    userVip.setUserDeposit(cash_fee);
                    ServiceResponse response = userVipService.updateUdepositByUserId(userVip);
                    if (response.isSuccess()) {
                        System.out.println("钱包充值成功(mysql)");
                    } else {
                        System.out.println("钱包充值失败(mysql)");
                    }
                }
            } else {
                System.out.println("订单记录和openid不是用一个人");
            }
        }
    }

    /**
     * 订单状态查询
     *
     * @param out_trade_no 商户订单号
     * @return
     */
    @Override
    public ServiceResponse<Object> orderQueryZT(String out_trade_no) {
        try {

            Map<String, String> paraMap = new TreeMap<String, String>();
            paraMap.put("appid", Configure.getAppID());
            paraMap.put("mch_id", Configure.getMch_id());
            paraMap.put("nonce_str", RandomStringGenerator.getRandomStringByLength(32));
            //二选一
            paraMap.put("out_trade_no", out_trade_no);
//          paraMap.put("transaction_id",request.getParameter("transaction_id"));
            String stringA = Signature.formatUrlMap(paraMap, false, false);
            String sign = MD5.MD5Encode(stringA + "&key=" + Configure.getKey()).toUpperCase();
            paraMap.put("sign", sign);
            String sxml = PayUtil.MaptoXml(paraMap);
            String ordrxml = PayRequest.httpRequest(Configure.QUERY_URL, "POST", sxml);
            System.out.println(ordrxml);
            Map paraminfo = PayUtil.doXMLParse(ordrxml);
            //xml转换对象
//            XStream xStream = new XStream();
//            xStream.alias("xml", OrderQueryParamsInfo.class);
//            OrderQueryParamsInfo orderQuerypostInfo = (OrderQueryParamsInfo)xStream.fromXML(ordrxml);
            if (paraminfo.get("return_code").equals("SUCCESS") && paraminfo.get("return_code").equals(paraminfo.get("result_code"))) {
                //更新订单
                //判断交易状态是否成功
                Orderpay orderpay = new Orderpay();
                System.out.println(paraminfo.get("out_trade_no"));
                if (paraminfo.get("trade_state").equals("SUCCESS")) {
                    orderpay.setTradeStatus((String) paraminfo.get("trade_state"));
                    orderpay.setTransactionId((String) paraminfo.get("transaction_id"));
                    orderpay.setOutTradeNo((String) paraminfo.get("out_trade_no"));
                    int getrows = orderPayService.updateOrder_statusByout_trade_no(orderpay);
                    if (getrows > 0) {
                        return ServiceResponse.createBySuccess("更新订单成功", paraminfo);
                    } else {
                        L.warn(out_trade_no + "更新订单导入数据库失败");
                        return ServiceResponse.createByError("更新订单导入数据库失败", paraminfo);
                    }
                } else {
                    orderpay.setTradeStatus((String) paraminfo.get("trade_state"));
                    orderpay.setOutTradeNo((String) paraminfo.get("out_trade_no"));
                    int getrows = orderPayService.updateOrder_statusByout_trade_no(orderpay);
                    if (getrows > 0) {
                        return ServiceResponse.createBySuccess("更新订单成功", paraminfo);
                    } else {
                        L.warn(out_trade_no + "更新订单导入数据库失败");
                        return ServiceResponse.createByError("更新订单导入数据库失败", paraminfo);
                    }
                }
//                    return ServiceResponse.createBySuccess(orderQuerypostInfo);
            } else {
                L.warn(out_trade_no + "更新订单导入数据库失败");
                return ServiceResponse.createByError("查询失败," + paraminfo.get("err_code_des"), paraminfo);
            }

        } catch (Exception e) {
            L.error("微信查询订单失败！", e);
        }
        return ServiceResponse.createByError(null);
    }

    /**
     * 关闭订单
     *
     * @param out_trade_no
     * @return
     */
    @Override
    public ServiceResponse<Object> ClosePay(String out_trade_no) {
        try {
            Map<String, String> paraMap = new TreeMap<String, String>();
            paraMap.put("appid", Configure.getAppID());
            paraMap.put("mch_id", Configure.getMch_id());
            //二选一
            paraMap.put("out_trade_no", out_trade_no);
            // paraMap.put("transaction_id",request.getParameter("transaction_id"));
            paraMap.put("nonce_str", RandomStringGenerator.getRandomStringByLength(32));
            String stringA = Signature.formatUrlMap(paraMap, false, false);
            String sign = MD5.MD5Encode(stringA + "&key=" + Configure.getKey()).toUpperCase();
            paraMap.put("sign", sign);
            String sxml = PayUtil.MaptoXml(paraMap);
            String ordrxml = PayRequest.httpRequest(Configure.Close_URL, "POST", sxml);
            //xml转换对象
            System.out.println(ordrxml);
            XStream xStream = new XStream();
            xStream.alias("xml", OrderCloseInfo.class);
            OrderCloseInfo orderCloseInfo = (OrderCloseInfo) xStream.fromXML(ordrxml);
            if (orderCloseInfo.getReturn_code().equals("SUCCESS")) {
                if (orderCloseInfo.getResult_code().equals(orderCloseInfo.getReturn_code())) {
                   ServiceResponse closeInfoResponse = this.gbClose(orderCloseInfo,out_trade_no);
                   return closeInfoResponse;

                } else {
                    return ServiceResponse.createByError(orderCloseInfo);
                }
            } else {
                return ServiceResponse.createByError(orderCloseInfo);
            }
        } catch (Exception e) {
            L.error("订单关闭失败" + e);
        }
        return ServiceResponse.createByError(null);
    }

    //订单关闭修改订单数据
    public ServiceResponse<OrderCloseInfo> gbClose(OrderCloseInfo orderCloseInfo, String out_trade_no){
        Orderpay orderpay = orderPayService.selectByouttradeno(out_trade_no);
        if(orderpay!=null){
            orderpay.setTradeStatus("CLOSED");
            int getrows = orderPayService.updateOrder_statusByout_trade_no(orderpay);
            if(getrows>0){
                return ServiceResponse.createBySuccess("订单关闭成功", orderCloseInfo);
            } else {
                L.warn(out_trade_no + "订单数据库删除失败");
                return ServiceResponse.createByError("订单数据库删除失败", orderCloseInfo);
            }
        }
        OrderActivity orderActivity = new OrderActivity();
        orderActivity.setOutTradeNo(out_trade_no);
        orderActivity.setTradeStatus("CLOSED");
        ServiceResponse activityResponse = orderActivityService.updateByOutTradeNo(orderActivity);
        if(activityResponse.isSuccess()){
            return ServiceResponse.createBySuccess("订单关闭成功", orderCloseInfo);
        } else {
            L.warn(out_trade_no + "订单数据库删除失败");
            return ServiceResponse.createByError("订单数据库删除失败", orderCloseInfo);
        }
    }
    /**
     * 30分钟内没有支付,自动关闭订单
     *
     * @param out 订单号
     */
    @Override
    public void orderClose(String out) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                try {
                    ServiceResponse<OrderActivity> orderActivityResponse= orderActivityService.selectOrderActivity(out);
                    ServiceResponse closePayResponse = null;
                    if(orderActivityResponse.isSuccess()) {
                        OrderActivity orderActivity = orderActivityResponse.getData();
                        if (orderActivity.getTradeStatus().equals("NOTPAY")) {
                            closePayResponse = ClosePay(orderActivity.getOutTradeNo());
                        }
                    }else{
                        Orderpay orderpay = orderPayService.selectByouttradeno(out);
                        if (orderpay.getTradeStatus().equals("NOTPAY")) {
                            closePayResponse = ClosePay(orderpay.getOutTradeNo());
                        }
                    }
                    if (closePayResponse.isSuccess()) {
                        System.out.println("订单关闭");
                    } else {
                        System.out.println("关闭失败");
                        L.warn(out + "订单关闭失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 中断线程
                timer.cancel();
            }
        }, 30 * 1000*60);
    }
}

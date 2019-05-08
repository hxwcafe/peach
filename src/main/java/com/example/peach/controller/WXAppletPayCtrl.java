package com.example.peach.controller;


import com.example.peach.common.Conts;
import com.example.peach.common.ServiceResponse;
import com.example.peach.configuration.Configure;
import com.example.peach.pojo.Activity;
import com.example.peach.pojo.Orderpay;
import com.example.peach.pojo.pay.OrderQueryParamsInfo;
import com.example.peach.service.ActivityService;
import com.example.peach.service.OrderPayService;
import com.example.peach.service.UserService;
import com.example.peach.service.WXPayService;
import com.example.peach.util.*;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import static com.example.peach.util.PayUtil.MaptoXml;

/**
 * 商户充值
 */
@RestController
@RequestMapping(value = "/api/pay")
public class WXAppletPayCtrl {
    @Resource
    private OrderPayService orderPayService;
    @Resource
    private WXPayService wxPayService;
    @Resource
    private UserService userService;
    @Resource
    private ActivityService activityService;
    private static final Logger L = Logger.getLogger(WXAppletPayCtrl.class);
    //交易类型

    /**
     * 活动付款
     * @param request id,openid
     * @return
     * @throws IllegalAccessException
     */
    @RequestMapping(value = "/weixin/payactivity", method = RequestMethod.POST)
    public ServiceResponse<Map> payActivity(HttpServletRequest request) throws IllegalAccessException {
        ServiceResponse<Map> activityResponse = activityService.selectById(Integer.valueOf(request.getParameter("id")));
        Activity activity = (Activity) activityResponse.getData().get("activity");
        Map map = wxPayService.actualMoney(request.getParameter("openid"),activity);
        ServiceResponse<Map> response = wxPayService.paymettwe(map,request);
        return response;
    }
    /**
     * 购买vip
     *
     * @param  request ff
     * @return
     * @throws ParseException
     * @throws IllegalAccessException
     */
    @RequestMapping(value = "/weixin/payvip", method = RequestMethod.POST)
    public ServiceResponse<Map> payVip(HttpServletRequest request) throws ParseException, IllegalAccessException {
        ServiceResponse openidresponse = userService.selectByOpenid(request.getParameter("openid"), Conts.OPENID);
        if (openidresponse.isSuccess()) {
            ServiceResponse orderPayresponse = orderPayService.Pricerevision(request.getParameter("openid"), 2);
            if (orderPayresponse.isSuccess()) {
                ServiceResponse<Map> response1 = wxPayService.paymettwe((Map) orderPayresponse.getData(),request);
                if(response1.isSuccess()){
                    return response1;
                }else{
                    return response1;
                }

            } else {
                return orderPayresponse;
            }
        } else {
            return ServiceResponse.createByError("您还没有进行授权", null);
        }
    }

    /**
     * 充值
     *
     * @param request
     * @return
     * @throws IllegalAccessException
     */
    @RequestMapping(value = "/weixin/recharge", method = RequestMethod.POST)
    public ServiceResponse<Map> payRecharge(HttpServletRequest request) throws IllegalAccessException {
        Map map = new HashMap();
        Double money = Double.valueOf(50);
        map.put("money", money);
        map.put("openid", request.getParameter("openid"));
        map.put("commodityId", 0);
        map.put("body", "他她派押金充值:" + money + "元");
        ServiceResponse<Map> response = wxPayService.paymettwe(map,request);
        return response;
    }



    /**
     * 支付成功回调函数
     *
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping(value = "/weixin/WXpayBack")
    public void PayNotify(HttpServletResponse response, HttpServletRequest request) throws Exception {
        System.out.println("已进入微信支付回调接口-----订单支付完成回调更新订单状态");
        //读取参数
        InputStream inputStream;
        StringBuffer sb = new StringBuffer();
        inputStream = request.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }
        bufferedReader.close();
        inputStream.close();
        String notify = sb.toString();
        String resXml = "";
        System.out.println("接受到的文件" + notify);
        Map map = PayUtil.doXMLParse(notify);
        String returnCode = (String) map.get("return_code");
        if ("SUCCESS".equals(returnCode)) {
            //验证签名是否正确
            Map<String, String> tonullmap = PayUtil.paraFilter(map);//回调验签时需要去除sign和空值参数
            String vailder = PayUtil.createLinkString(tonullmap);//把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
            String sign = PayUtil.sign(vailder, Configure.getKey(), "utf-8").toUpperCase();//拼装生成服务器端验证的签名
            //根据微信官网的介绍，此处不仅对回调的参数进行验签，还需要对返回的金额与系统订单的金额进行比对
            if (sign.equals(map.get("sign"))) {
                ServiceResponse orderqueryResponse = wxPayService.orderQueryZT((String) map.get("out_trade_no"));
                OrderQueryParamsInfo orderpayInfo = (OrderQueryParamsInfo) orderqueryResponse.getData();//查询订单状态
                if (orderpayInfo.getTotal_fee() == (int)map.get("total_fee")&&orderpayInfo.getTrade_state().equals("SUCCESS")) {
                    Orderpay orderpay = new Orderpay();
                    orderpay.setTransactionId((String) map.get("transaction_id"));
                    orderpay.setUpdateTime((String) map.get("time_end"));
                    orderpay.setTradeStatus("SUCCESS");
                    int getrows = orderPayService.updateOrder_statusByout_trade_no(orderpay);
                    if (getrows > 0) {
                        System.out.println("数据库订单状况更新成功");
                    } else {
                        L.warn("数据库订单状况更新失败");
                    }
                    wxPayService.wxpayBackmysql((String) map.get("openid"), (String) map.get("out_trade_no"), (Double) map.get("cash_fee"));
                    //通知微信服务器已经支付成功
                    resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                            + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    System.out.println("回调支付成功");
                } else {
                    L.warn("微信支付回调失败!金额不一致");

                }
            } else {
                L.warn("微信支付回调失败!签名不一致");
            }
        } else {
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        System.out.println("resXml:" + resXml);
        System.out.println("微信支付回调结束");

        BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        outputStream.write(resXml.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 订单查询
     *
     * @param request out_trade_no商户号
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/orderquery", method = RequestMethod.POST)
    public ServiceResponse<Object> orderQuery(HttpServletRequest request) throws Exception {
        ServiceResponse<Object> orderQueryZTresponse = wxPayService.orderQueryZT(request.getParameter("out_trade_no"));
        return orderQueryZTresponse;
    }

    /**
     * 订单关闭
     * @param request out_trade_no商户订单
     * @return
     */
    @RequestMapping(value = "/closepay", method = RequestMethod.POST)
    public ServiceResponse<Object> ClosePay(HttpServletRequest request) {
        ServiceResponse closerequse = wxPayService.ClosePay(request.getParameter("out_trade_no"));
        return closerequse;
    }



    /**
     * 微信申请退款
     *
     * @param out_trade_no 商户订单号
     * @param total_fee    订单金额  微信金额的单位是分 所以这里要X100 转成int是因为 退款的时候不能有小数点
     * @return
     */
    @RequestMapping(value = "/refunpay",method = RequestMethod.POST)
    public ServiceResponse<Map> RefundPay(String out_trade_no, double total_fee) {
        long money=Math.round(total_fee*100);
        String param = PayUtil.wxPayRefund(out_trade_no, String.valueOf(money),null);
        System.out.println("支付退款签名:" + param);
        String result = "";
        Map map = null;
        try {
            result = CertUtil.refund(param, Configure.Refund_URL);
            System.out.println(result);
            Map paraminfo = PayUtil.doXMLParse(result);//xml转map
            if ("SUCCESS".equals(paraminfo.get("return_code"))) {
                if ("SUCCESS".equals(paraminfo.get("result_code"))) {
                    //退款成功的操作
                    String prepay_id = (String) paraminfo.get("prepay_id");//返回的预付单信息
                    System.out.println(prepay_id);
                    map.put("transaction_id", paraminfo.get("transaction_id"));//微信订单号
                    map.put("out_refund_no", paraminfo.get("out_refund_no"));//商户退款单号
                    map.put("out_trade_no", paraminfo.get("out_trade_no"));//商户订单号
                    map.put("refund_id", paraminfo.get("refund_id"));//微信退款单号
                    map.put("refund_fee", paraminfo.get("refund_fee"));//退款金额
                    return ServiceResponse.createBySuccess(map);
                } else {
                    System.out.println("退款失败:原因" + paraminfo.get("err_code_des"));
                    return ServiceResponse.createByError(paraminfo);
                }
            } else {
                System.out.println("退款失败:原因" + paraminfo.get("return_msg"));
                return ServiceResponse.createByError(paraminfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Pattern p = Pattern.compile("");
        return ServiceResponse.createByError(null);
    }

    // 获取需要发送的url地址

    // 企业付款查询
    private static final String TRANSFERS_PAY_QUERY = "https://api.mch.weixin.qq.com/mmpaymkttransfers/gettransferinfo";

    /**
     * 企业转帐个人账户
     * @param request
     * @return amount提现金额  openid
     */
    @RequestMapping(value = "/withdraw",method = RequestMethod.POST)
    public ServiceResponse<String> weixinWithdraw(HttpServletRequest request) {
        String desc = "他她派提现" + request.getParameter("amount") + "元";
        Double money = Double.parseDouble(request.getParameter("amount"));
        long money2 = Math.round(money * 100);
        String amount = String.valueOf(money2);
        System.out.println(amount);
        try {
            Map<String, String> parameters = new TreeMap<String, String>();
            parameters.put("mch_appid", Configure.getMch_appid());
            parameters.put("amount", amount);
            parameters.put("check_name","NO_CHECK");
            parameters.put("mchid", Configure.getMch_id());
            parameters.put("partner_trade_no", RandomStringGenerator.getRandomStringtime(32));
            parameters.put("nonce_str", RandomStringGenerator.getRandomStringByLength(32));
            parameters.put("openid", request.getParameter("openid"));
            parameters.put("spbill_create_ip", Configure.getSpbill_create_ip());
            parameters.put("desc", desc);
            //签名
            Map<String, String> tonullmap = PayUtil.paraFilter(parameters);
            String stringA = Signature.formatUrlMap(tonullmap, false, false);
            System.out.println("签名:"+stringA);
            String sign = MD5.MD5Encode(stringA+"&key="+ Configure.getKey()).toUpperCase();
            parameters.put("sign", sign);
            String data = MaptoXml(parameters);
            System.out.println(data);
            String result = CertUtil.refund(data, Configure.TRANSFERS_PAY);
            System.out.println(result);
            //xml转换对象
//            XStream xStream = new XStream();
//            xStream.alias("xml", TransfersInfo.class);
//            TransfersInfo trainfo = (TransfersInfo) xStream.fromXML(result);
            Map paraminfo = PayUtil.doXMLParse(result);
            if("SUCCESS".equals(paraminfo.get("return_code"))) {
                if (paraminfo.get("return_code").equals(paraminfo.get("result_code"))) {

                    return ServiceResponse.createBySuccess();
                } else {
                    L.info("转账失败" + paraminfo.get("err_code_des"));
                    return  ServiceResponse.createByError((String) paraminfo.get("err_code_des"));
                }
            }else{
                L.info("转账失败" + paraminfo.get("return_msg"));
                return  ServiceResponse.createByError((String) paraminfo.get("return_msg"));
            }
        } catch (Exception e) {
            L.error("企业付款异常" + e.getMessage(), e);
        }
        return null;
    }

    //查询企业付款到个人url
    private static final String GETTRANSFERS_PAY = "https://api.mch.weixin.qq.com/mmpaymkttransfers/gettransferinfo";

    public Map getweixinWithdraw() {
        Map map = new HashMap();
        try {
            Map<String, String> params = new HashMap<String, String>();
        } catch (Exception e) {
            e.printStackTrace();
            L.error("企业付款异常" + e.getMessage(), e);
        }

        return null;
    }
}

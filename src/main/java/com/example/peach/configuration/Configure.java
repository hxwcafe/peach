package com.example.peach.configuration;

/**
 * 商户配置
 */
public class Configure {

    // 商户支付秘钥
    private static String key = "b4f78c2a192fb51f1417eefdf2ae9b74";
    //小程序ID
    private static String appID = "wxd472817c47fa123a";
    //申请商户号的appid
    private static String mch_appid = "wxc5a22a068cd7149e";
    //商户号
    private static String mch_id = "1519183381";
    // 小程序的secret
    private static String secret = "9108525c9fcaa6a42fb79fb8f79270b2";
    //Ip地址
    private static String spbill_create_ip = "192.168.1.109";

    //获取openid
    public static final String Code_Session = "https://api.weixin.qq.com/sns/jscode2session";
    //统一下单API接口链接
    public static final String TYURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    //查询API接口
    public static final String QUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
    //关闭订单API
    public static final String Close_URL = "https://api.mch.weixin.qq.com/pay/closeorder";
    //退款
    public static final String Refund_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    //企业给个人付款
    public static final String TRANSFERS_PAY = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";

    public static String getSecret() {
        return secret;
    }

    public static void setSecret(String secret) {
        Configure.secret = secret;
    }

    public static String getKey() {
        return key;
    }

    public static void setKey(String key) {
        Configure.key = key;
    }

    public static String getAppID() {
        return appID;
    }

    public static void setAppID(String appID) {
        Configure.appID = appID;
    }

    public static String getMch_id() {
        return mch_id;
    }

    public static void setMch_id(String mch_id) {
        Configure.mch_id = mch_id;
    }

    public static String getMch_appid() {
        return mch_appid;
    }

    public static void setMch_appid(String mch_appid) {
        Configure.mch_appid = mch_appid;
    }

    public static String getSpbill_create_ip() {
        return spbill_create_ip;
    }

    public static void setSpbill_create_ip(String spbill_create_ip) {
        Configure.spbill_create_ip = spbill_create_ip;
    }
}

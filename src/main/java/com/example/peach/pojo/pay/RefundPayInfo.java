package com.example.peach.pojo.pay;

public class RefundPayInfo {
    //业务结果
    private String result_code;
    //返回信息
    private String return_msg;
    //小程序ID
    private String appid;
    //商户号
    private String mch_id;
    //随机字符串
    private String nonce_str;
    //签名
    private String sign;
    //微信订单号
    private String transaction_id;
    //商品订单号
    private String out_trade_no;
    //商户退款单号
    private String out_refund_no;
    //微信退款单号
    private String refund_id;
    //退款金额
    private String refund_fee;
    //标价金额
    private int total_fee;
    //现金支付金额
    private int cash_fee;

}

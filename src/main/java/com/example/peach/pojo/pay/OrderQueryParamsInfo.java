package com.example.peach.pojo.pay;

/**
 * 订单查询返回信息
 */
public class OrderQueryParamsInfo {
    // 返回状态码，通信标识，SUCCESS/FAIL
    private String return_msg;
    private String return_code;

    // 公众账号id
    private String appid;

    // 商户号
    private String mch_id;

    // 随机字符串
    private String nonce_str;

    // 签名
    private String sign;

    // 业务结果，交易标识，SUCCESS/FAIL
    private String result_code;

    // 用户标识
    private String openid;

    // 交易类型，JSAPI，NATIVE，APP
    private String trade_type;

    // 交易状态，SUCCESS-成功 USERPAYING-支付中
    private String trade_state;

    // 付款银行
    private String bank_type;

    // 标价金额，单位分
    private int total_fee;

    // 现金支付金额
    private int cash_fee;

    // 微信支付订单号
    private String transaction_id;

    // 商户订单号
    private String out_trade_no;
    // 支付完成时间
    private String time_end;
    //附加数据
    private String attach;
    // 交易状态描述
    private String trade_state_desc;
    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getTrade_state() {
        return trade_state;
    }

    public void setTrade_state(String trade_state) {
        this.trade_state = trade_state;
    }

    public String getBank_type() {
        return bank_type;
    }

    public void setBank_type(String bank_type) {
        this.bank_type = bank_type;
    }

    public int getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }

    public int getCash_fee() {
        return cash_fee;
    }

    public void setCash_fee(int cash_fee) {
        this.cash_fee = cash_fee;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public String getTrade_state_desc() {
        return trade_state_desc;
    }

    public void setTrade_state_desc(String trade_state_desc) {
        this.trade_state_desc = trade_state_desc;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }
}

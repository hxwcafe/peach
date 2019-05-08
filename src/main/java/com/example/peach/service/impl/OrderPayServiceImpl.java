package com.example.peach.service.impl;

import com.example.peach.common.Conts;
import com.example.peach.common.ServiceResponse;
import com.example.peach.dao.CommodityMapper;
import com.example.peach.dao.OrderpayMapper;
import com.example.peach.dao.UserMapper;
import com.example.peach.dao.UserVipMapper;
import com.example.peach.pojo.Commodity;
import com.example.peach.pojo.Orderpay;
import com.example.peach.pojo.User;
import com.example.peach.service.OrderPayService;
import com.example.peach.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 1.用户请求下单,调用登录API()获取到Openid(),生成商户订单orderid()
 * 2.拿到openid()调用支付统一下单接口,
 *                          (1)必须要需要的参数:appid(小程序ID),mch_id,nonce_str(商户号),sign(签名:签名生成算法),body(随机字符串:使用随机函数生成),
 *                          out_trade_no(商户订单号:使用随机函数生成),total_fee(标价金额),spbill_create_ip(终端IP),openid(用户唯一标识),
 *                           notify_url(通知地址),trade_type(交易类型;JSAPI--公众号支付)
 *                           (2)转化为xml,if(return_code 和result_code都为SUCCESS),生成签名appId,timeStamp(时间戳),nonceStr(随机字符串),package(统一下单接口返回的 prepay_id),signType(签名方式:MD5),paySign(二次生成的签名),
 * 3.支付成功回调函数
 */
@Service
public class OrderPayServiceImpl implements OrderPayService {
    @Resource
    private OrderpayMapper orderpayMapper;
    @Resource
    private UserService userService;
    @Resource
    private UserMapper userMapper;
    @Resource
    private CommodityMapper commodityMapper;
    @Resource
    private UserVipMapper userVipMapper;
    /**
     * 插入用户订单信息
     *
     * @param orderpay
     * @return
     */
    @Override
    public int insertOrderPay(Orderpay orderpay) {

        int getrows = orderpayMapper.insert(orderpay);
        return getrows;
    }

    /**
     * 根据微信订单号修改订单信息
     *
     * @param orderpay
     * @return
     */
    @Override
    public int updateOrder_statusBytrade_status(Orderpay orderpay) {
        int getrows = orderpayMapper.updateOrder_statusBytrade_status(orderpay);
        return getrows;
    }

    /**
     * 根据商户订单号修改订单信息
     *
     * @param orderpay
     * @return
     */
    @Override
    public int updateOrder_statusByout_trade_no(Orderpay orderpay) {
        int getrows = orderpayMapper.updateOrder_statusByout_trade_no(orderpay);
        return getrows;
    }


    /**
     * 根据用户id查询用户订单历史
     *
     * @param user_id
     * @return
     */
    @Override
    public ServiceResponse<List> selectOrderByuserId(int user_id) {
        List<Orderpay> list = new ArrayList<Orderpay>();
                list=orderpayMapper.selectOrderByuserid(user_id);
        if (list == null) {
            return ServiceResponse.createByError("查询失败", null);
        }
        return ServiceResponse.createBySuccess("查询成功", list);
    }

    /**
     * 删除订单
     *
     * @param outTradeNo
     * @return
     */
    @Override
    public int delectByoutTradeNo(String outTradeNo) {
        int getrows = orderpayMapper.deleteByout_trade_no(outTradeNo);
        return getrows;
    }

    /**
     * 会员购买
     * @param openid
     * @param id
     * @return
     * @throws ParseException
     */
    @Override
    public ServiceResponse<Map> Pricerevision(String openid, int id) throws ParseException {
        ServiceResponse response = userService.selectByOpenid(openid,Conts.NEWOLD);

        Map map = new HashMap();
        map.put("openid", openid);

        Commodity commodity = commodityMapper.selectByPrimaryKey(id);
        //产品报价
        Double money = commodity.getCommodityPrice();
        //产品信息
        String body = "购买"+commodity.getCommodityName()+","+commodity.getCommodityBody();
        map.put("body",body);
        map.put("commodityId",commodity.getId());
        User user = userMapper.selectByOpenid(openid);
        //判定用户是否存在
        if (user != null) {
            //判断用户是否新老用户
            if (response.isSuccess()) {
                //报价格-首次优惠金额
                money-=commodity.getFirstdiscount();
                map.put("money",money);
                return ServiceResponse.createBySuccess(map);
            } else {
                int vip=userVipMapper.selectByUserId(user.getId()).getId();
                Orderpay orderpay = orderpayMapper.selectByUseridAndCommodityid(user.getId(), vip,"SUCCESS");
                //是否有此购买记录
                if (orderpay != null) {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date d1 = df.parse(orderpay.getEndTime());
                    Date d2 = df.parse(df.format(new Date()));
                    long days = (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24);
                    System.out.println("离上次购买会员的时间相差:" + days);
                    Double money2 = commodityMapper.selectByPrimaryKey(vip).getCommodityPrice();
                    //是否在购买的时间以内
                    if (days <= 30) {
                        //购买的卡比原卡还low
                        if (money > money2) {
                            money -= money2;
                            map.put("money", money);
                            return ServiceResponse.createBySuccess(map);
                        } else {
                            return ServiceResponse.createByError("你购买的卡比原卡等级还要低", map);
                        }
                    } else {
                        map.put("money", money);
                        return ServiceResponse.createBySuccess(map);
                    }


                }else{
                    return ServiceResponse.createByError("没有购买记录",map);
                }

            }
        } else {
           return ServiceResponse.createByError("用户不存在",null);
        }
    }

    /**
     * 根据商户单号查询信息
     * @param outTradeNo
     * @return
     */
    @Override
    public Orderpay selectByouttradeno(String outTradeNo) {
        Orderpay orderpay = orderpayMapper.selectByouttradeno(outTradeNo);
        if(orderpay!=null){
            return orderpay;
        }else{
            return  orderpay;
        }

    }


}


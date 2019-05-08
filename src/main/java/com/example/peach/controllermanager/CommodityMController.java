package com.example.peach.controllermanager;

import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.Commodity;
import com.example.peach.pojo.OrderRefund;
import com.example.peach.pojo.merge.OrderRAct;
import com.example.peach.service.AdminService;
import com.example.peach.service.CommodityService;
import com.example.peach.service.OrderRefundService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/manager")
public class CommodityMController {
    @Resource
    private CommodityService commodityService;
    @Resource
    private OrderRefundService orderRefundService;
    @Resource
    private AdminService adminService;
    /**
     * 添加vip商品
     * @param commodity
     * @return
     */
    @RequestMapping(value = "/addcommodity",method = RequestMethod.POST)
    public ServiceResponse<String> addCommodity(Commodity commodity){
        ServiceResponse<String> commodityServiceResponse = commodityService.insertSelective(commodity);
        return commodityServiceResponse;
    }

    /**
     * 修改vip卡信息
     * @param commodity
     * @return
     */
    @RequestMapping(value = "/xgcommodity",method = RequestMethod.POST)
    public ServiceResponse<String> upadateCommodity(Commodity commodity){
        ServiceResponse<String> commodotyServiceResponse = commodityService.updateCommodity(commodity);
        return commodotyServiceResponse;
    }

    /**
     * 删除vip卡信息
     * @param id vip卡id
     * @return
     */
    @RequestMapping(value = "/sccommodity",method = RequestMethod.POST)
    public ServiceResponse<String> delCommodity(Integer id){
        ServiceResponse<String> commodotyServiceResponse = commodityService.delCommodity(id);
        return commodotyServiceResponse;
    }

    /**
     * 查询所有退款订单
     * @return true(List OrderRAct) false
     */
    @RequestMapping(value = "/allrefund",method = RequestMethod.GET)
    public List<OrderRAct> selectAllOrderRefund(){
        return orderRefundService.selectallOrderRAct();
    }
    /**
     * 删除退款订单
     * @param id 订单id
     * @return true false
     */
    @RequestMapping(value = "/delrefund",method = RequestMethod.POST)
    public ServiceResponse<String> deleteRefund(Integer id, HttpServletRequest request){
        ServiceResponse<String> lognResponse = adminService.judgelognAdmin(request);
        if(!lognResponse.isSuccess()) {
            return lognResponse;
        }
        return orderRefundService.deleteRefund(id);
    }

    /**
     * 退还押金,
     * @param orderRefund 退款表
     * @param out_trade_no 商户单号
     * @param total_fee 退款金额
     * @return true false
     */
    @RequestMapping(value = "/modifyrefund",method = RequestMethod.POST)
    public ServiceResponse updateRefund(OrderRefund orderRefund,String out_trade_no, String total_fee,HttpServletRequest request){
        ServiceResponse<String> lognResponse = adminService.judgelognAdmin(request);
        if(!lognResponse.isSuccess()) {
            return lognResponse;
        }
        if (orderRefund.getStatus()==1){
            return orderRefundService.RefundPay(out_trade_no, Double.parseDouble(total_fee),orderRefund);
        }else if(orderRefund.getStatus()==2){
            return orderRefundService.upRefundandUserdeposit(orderRefund);
        }
        return ServiceResponse.createByError("选择错误");
    }
}

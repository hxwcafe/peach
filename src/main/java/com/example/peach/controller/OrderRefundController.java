package com.example.peach.controller;

import com.example.peach.common.ServiceResponse;
import com.example.peach.service.OrderRefundService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/refund")
public class OrderRefundController {
    @Resource
    private OrderRefundService orderRefundService;

    /**
     * 插入订单
     * @param userId 用户id
     * @return true false(0 没有缴纳押金,1 您已经有一个退款正在处理中,2 退款插入失败)
     */
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    public ServiceResponse insertOrderRefund(Integer userId){
        return orderRefundService.insertOrderRefund(userId);
    }
}

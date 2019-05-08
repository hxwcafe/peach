package com.example.peach.controller;

import com.example.peach.common.ServiceResponse;
import com.example.peach.service.OrderPayService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class OrderController {
    @Resource
    private OrderPayService orderPayService;
    /**
     * 订单历史
     * @param userId
     * @return
     */
    @RequestMapping(value = "/weixin/bill", method = RequestMethod.GET)
    public ServiceResponse<List> Billinghistory(Integer userId) {
        ServiceResponse<List> response = orderPayService.selectOrderByuserId(userId);
        return response;
    }
}

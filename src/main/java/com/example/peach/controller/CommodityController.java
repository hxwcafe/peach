package com.example.peach.controller;

import com.example.peach.common.ServiceResponse;
import com.example.peach.service.CommodityService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/commvip")
public class CommodityController {
    @Resource
    private CommodityService commodityService;

    /**
     * 所有信息展示
     * @return
     */
    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public ServiceResponse<List> selectCommodity(){
        ServiceResponse<List> response = commodityService.selectCommodity();
        return response;
    }
}

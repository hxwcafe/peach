package com.example.peach.service;

import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.Commodity;

import java.util.List;

public interface CommodityService {
    //显示所有会员卡信息
    ServiceResponse<List> selectCommodity();
    //添加商品信息
    ServiceResponse<String> insertSelective(Commodity record);
    //修改信息
    ServiceResponse<String> updateCommodity(Commodity commodity);
    //删除信息
    ServiceResponse<String> delCommodity(Integer id);
}
